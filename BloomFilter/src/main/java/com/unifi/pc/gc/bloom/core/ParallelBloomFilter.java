package com.unifi.pc.gc.bloom.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ParallelBloomFilter implements Runnable {
	private BloomFilter filter;
	private Semaphore analysisPhaseSem, reportPhaseSem;
	private List sample, dataFlows;
	private ExecutorService myThreads;
	private int numThreads;
	private int rejectedNumber;
	private int admittedNumber;
	
	private List chunksListSample;
	private List chunksListDataFlow;

	// set the threads
	public ParallelBloomFilter(BloomFilter filter, List sample, List dataFlows, int threads) {
		this.filter = filter;
		// training set
		this.sample = sample;
		this.numThreads = threads;
		// test set (a part of sample)
		this.dataFlows = dataFlows;
		this.analysisPhaseSem = new Semaphore(numThreads);
		this.reportPhaseSem = new Semaphore(numThreads);
		this.rejectedNumber = 0;
		this.admittedNumber = 0;

		myThreads = Executors.newFixedThreadPool(numThreads);
		
		InitializeBloomThreads.setBloomFilter(filter);
		AnalyzeBloomThreads.setBloomFilter(filter);
	}
	
	
	

	public int getRejectedNumber() {
		return rejectedNumber;
	}




	public int getAdmittedNumber() {
		return admittedNumber;
	}

	public List getChunksListSample() {
		return chunksListSample;
	}

	public List getChunksListDataFlow() {
		return chunksListDataFlow;
	}




	@Override
	public void run() {
		try {
			reportPhaseSem.acquire(numThreads);
			createInitThreads();
			createAnalyzeThreads();
			reportPhaseSem.acquire(numThreads);
			myThreads.shutdown();
			if (!myThreads.awaitTermination(60000, TimeUnit.SECONDS)) {
			    System.err.println("Threads didn't finish in 60000 seconds!");
			}
		} catch (InterruptedException e) {
			System.out.println("Semaphore 2 error");
			System.exit(-1);
		}
	}

	private void createInitThreads() throws InterruptedException {
		analysisPhaseSem.acquire(numThreads);
		InitializeBloomThreads.setAnalysisPhaseSem(analysisPhaseSem);
		threadsInitCreation(sample);
	}

	private void createAnalyzeThreads() throws InterruptedException {
		analysisPhaseSem.acquire(numThreads);
		AnalyzeBloomThreads.setReportPhaseSem(reportPhaseSem);
		//viewFilter(filter.viewPositionsOfOne());
		threadsAnalyzeCreation(dataFlows);
	}


	private void threadsInitCreation(List currentList) {
		int chunkDim = currentList.size() / numThreads;
		chunksListSample=new ArrayList();
		for (int i = 0; i < numThreads; i++) {
			int fromIndex, toIndex;
			fromIndex = i * chunkDim;
			if(i==numThreads-1) {
				toIndex=currentList.size();
			}else {
				toIndex = (i + 1) * chunkDim;
			}
			chunksListSample.addAll(currentList.subList(fromIndex, toIndex));
			Runnable currentInitializeBloomThread = new InitializeBloomThreads(
					currentList.subList(fromIndex, toIndex));
			
			myThreads.execute(currentInitializeBloomThread);
		}
	}


	private void threadsAnalyzeCreation(List currentList) {
		try {			
			int chunkDim = currentList.size() / numThreads;
			chunksListDataFlow=new ArrayList();
			for (int i = 0; i < numThreads; i++) {
				int fromIndex, toIndex;
				fromIndex = i * chunkDim;
				if(i==numThreads-1) {
					toIndex=currentList.size();
				}else {
					toIndex = (i + 1) * chunkDim;
				}
				chunksListDataFlow.addAll(currentList.subList(fromIndex, toIndex));
				Callable<List<Integer>> currentInitializeBloomThread = new AnalyzeBloomThreads(
						currentList.subList(fromIndex, toIndex));
				Future<List<Integer>> result = myThreads.submit(currentInitializeBloomThread);
				rejectedNumber = rejectedNumber + result.get().get(1);
				admittedNumber = admittedNumber + result.get().get(0);
			}
		} catch (InterruptedException e) {
			System.out.println("Semaphore 1 error");
			System.exit(-1);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public ExecutorService getMyThreads() {
		return myThreads;
	}
	
	
	private void viewFilter(int[] viewPositionsOfOne) {
		if(viewPositionsOfOne.length!=0) {
			String rst="[";
			for(int i=0; i<viewPositionsOfOne.length-1; i++) {
				rst=rst+viewPositionsOfOne[i]+", ";
			}
			rst=rst+viewPositionsOfOne[viewPositionsOfOne.length-1]+"]";
			
			System.out.println(rst);
		} else
			System.out.println("Matteo qualcosa non va: la lunghezza del vettore di bit è zero.\n\n");
	}
	
}

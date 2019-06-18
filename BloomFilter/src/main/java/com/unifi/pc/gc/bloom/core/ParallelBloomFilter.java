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
	private Semaphore analysisPhaseSem, reportPhaseSem;
	private List<String> sample, dataFlows;
	private ExecutorService myThreads;
	private int numThreads;
	private int rejectedNumber;
	private int admittedNumber;
	
	private List<String> chunksListSample;
	private List<String> chunksListDataFlow;

	//constructor
	public ParallelBloomFilter(BloomFilter<String> filter, List<String> sample, List<String> dataFlows, int threads) {
		this.sample = sample;
		this.numThreads = threads;
		this.dataFlows = dataFlows;
		this.analysisPhaseSem = new Semaphore(numThreads);
		this.reportPhaseSem = new Semaphore(numThreads);
		this.rejectedNumber = 0;
		this.admittedNumber = 0;

		myThreads = Executors.newFixedThreadPool(numThreads);
		
		InitializeBloomThreads.setBloomFilter(filter);
		AnalyzeBloomThreads.setBloomFilter(filter);
	}
	
	
	//getters
	public int getRejectedNumber() {
		return rejectedNumber;
	}

	public int getAdmittedNumber() {
		return admittedNumber;
	}

	public List<String> getChunksListSample() {
		return chunksListSample;
	}

	public List<String> getChunksListDataFlow() {
		return chunksListDataFlow;
	}


//parallel execution using initialization and analysis threads 
	public void run() {
		try {
			createInitThreads();
			createAnalyzeThreads();
     		myThreads.shutdown();
			if (!myThreads.awaitTermination(60000, TimeUnit.SECONDS)) {
			    System.err.println("Threads didn't finish in 60000 seconds!");
			}
		} catch (InterruptedException e) {
			System.out.println("Semaphore 2: error");
			System.exit(-1);
		}
	}

//initialization threads creation
	private void createInitThreads() throws InterruptedException {
		reportPhaseSem.acquire(numThreads);
		analysisPhaseSem.acquire(numThreads);
		InitializeBloomThreads.setAnalysisPhaseSem(analysisPhaseSem);
		executeInitThreads(sample);
	}

//initialization threads execution
	private void executeInitThreads(List<String> currentList) {
		int chunkDim = currentList.size() / numThreads;
		chunksListSample=new ArrayList<String>();
		for (int i = 0; i < numThreads; i++) {
			int fromIndex, toIndex;
			fromIndex = i * chunkDim;
			if(i==numThreads-1) {
				toIndex=currentList.size();
			}else {
				toIndex = (i + 1) * chunkDim;
			}
			chunksListSample.addAll(currentList.subList(fromIndex, toIndex));
			Runnable currentInitializeBloomThread = new InitializeBloomThreads(currentList.subList(fromIndex, toIndex));
			
			myThreads.execute(currentInitializeBloomThread);
		}
	}

//analysis threads creation
	private void createAnalyzeThreads() throws InterruptedException {
		analysisPhaseSem.acquire(numThreads);
		AnalyzeBloomThreads.setReportPhaseSem(reportPhaseSem);
		executeAnalyzeThreads(dataFlows);
	}
	
//analysis threads execution	
	private void executeAnalyzeThreads(List<String> currentList) throws InterruptedException {
		List<Future<List<Integer>>> resultList = new ArrayList<>();
			int chunkDim = currentList.size() / numThreads;
			int fromIndex, toIndex;
			chunksListDataFlow=new ArrayList<String>();
			for (int i = 0; i < numThreads; i++) {
				fromIndex=0; 
				toIndex=0;
				fromIndex = i * chunkDim;
				if(i==numThreads-1) {
					toIndex=currentList.size();
				}else {
					toIndex = (i + 1) * chunkDim;
				}
				chunksListDataFlow.addAll(currentList.subList(fromIndex, toIndex));
				Callable<List<Integer>> currentAnalyzeBloomThread = new AnalyzeBloomThreads(currentList.subList(fromIndex, toIndex));
				Future<List<Integer>> result = myThreads.submit(currentAnalyzeBloomThread);
				resultList.add(result);
			}
			reportPhaseSem.acquire(numThreads);
			
			for(Future<List<Integer>> future : resultList)
	          {
				try {
					rejectedNumber = rejectedNumber + future.get().get(1);
					admittedNumber = admittedNumber + future.get().get(0);

				} catch (ExecutionException e) {
					e.printStackTrace();
				}
	            }
			
	}
	
	

	public ExecutorService getMyThreads() {
		return myThreads;
	}

	
}

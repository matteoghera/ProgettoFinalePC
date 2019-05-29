package com.unifi.pc.gc.bloom.core;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;

import com.unifi.pc.gc.bloom.utility.HashFunction;
import com.unifi.pc.gc.bloom.utility.Tools;

public class ParallelBloomFilterTest {
	ParallelBloomFilter myParallelFilter;
	List<Integer> sample;
	List<Integer> dataFlow;
	Thread myThread;
	
	
	@Before
	public void setUp(){
		int[] elements = {356,678,432};
		sample=createSet(elements);
		int[] setElements= {345,654, 987, 567, 123, 456, 789, 978, 125, 145, 176};
		dataFlow=createSet(setElements);
		
		List<HashFunction<Integer>> myHashFunctionList=new ArrayList<HashFunction<Integer>>();	
		myHashFunctionList.add(new HashFunction<Integer>(10));
		BloomFilter<Integer> filter=new BloomFilter<Integer>(10, myHashFunctionList);
		int threads= 2;
		
		myParallelFilter=new ParallelBloomFilter (filter, sample, dataFlow, threads);
		
	}

	@Test
	public void testNumberThreads() throws InterruptedException, ExecutionException {
		int maxNumberOfThreads;
		Callable<Integer> myTestThreadsHandle=new TestThreadsHandle(myParallelFilter);
		ExecutorService myThreads=Executors.newFixedThreadPool(1);
		Future<Integer>result= myThreads.submit(myTestThreadsHandle);
		runParallelFilter();
		maxNumberOfThreads=result.get();
		myThreads.shutdown();
		assertEquals(4, maxNumberOfThreads);
	}
	
	@Test
	public void testChunkSample() {
		runParallelFilter();
		while(myThread.isAlive());
		List<Integer> result=myParallelFilter.getChunksListSample();
		assertEquals(sample, result);
	}
	
	
	@Test
	public void testChunkDataFlow() {
		runParallelFilter();
		while(myThread.isAlive());
		List<Integer> result=myParallelFilter.getChunksListDataFlow();
		assertEquals(dataFlow, result);
	}

	@Test
	public void testResult() {
		runParallelFilter();
		while(myThread.isAlive());
		assertEquals(8, myParallelFilter.getRejectedNumber());
		assertEquals(3, myParallelFilter.getAdmittedNumber());
	}

	private List<Integer> createSet(int[] elements) {
		List<Integer> mySet=new ArrayList<>();
		for(int i=0; i<elements.length; i++) {
			mySet.add(elements[i]);
		}
		return mySet;
	}
	
	private void runParallelFilter() {
		myThread=new Thread(myParallelFilter);
		myThread.start();
	}
	
	
}














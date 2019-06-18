package com.unifi.pc.gc.bloom.core;
import static org.junit.Assert.*;

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
import com.unifi.pc.gc.bloom.utility.SimpleHashFunction;

public class ParallelBloomFilterTest {
	ParallelBloomFilter myParallelFilter;
	List<String> sample;
	List<String> dataFlow;
	//Thread myThread;
	
	
	@Before
	public void setUp(){
		String[] elements = {"aaa","bbb","ccc"};
		sample=createSet(elements);
		String[] setElements= {"aaa","bbb","ccc", "ddd","eee","fff","ggg","hhh","iii","zzz"};
		dataFlow=createSet(setElements);
		
		List<HashFunction<String>> myHashFunctionList=new ArrayList<HashFunction<String>>();	
		myHashFunctionList.add(new SimpleHashFunction<String>(10));
		BloomFilter<String> filter=new BloomFilter<String>(10, myHashFunctionList);
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
		List<String> result=myParallelFilter.getChunksListSample();
		
		assertEquals(sample, result);
	}
	
	
	@Test
	public void testChunkDataFlow() {
		runParallelFilter();
		List<String> result=myParallelFilter.getChunksListDataFlow();
		assertEquals(dataFlow, result);
	}

	@Test
	public void testResult() {
		runParallelFilter();
		assertEquals(7, myParallelFilter.getRejectedNumber());
		assertEquals(3, myParallelFilter.getAdmittedNumber());
	}

	private List<String> createSet(String[] elements) {
		List<String> mySet=new ArrayList<>();
		for(int i=0; i<elements.length; i++) {
			mySet.add(elements[i]);
		}
		return mySet;
	}
	
	private void runParallelFilter() {
	myParallelFilter.execute();
	}
	
	
}





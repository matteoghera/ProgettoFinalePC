package com.unifi.pc.gc.bloom.core;

import java.util.List;
import java.util.concurrent.Semaphore;

public class InitializeBloomThreads  implements Runnable{
	private static BloomFilter<String>  filter;
	private List<String> chunk;
	private static Semaphore analysisPhaseSem;
	

	public InitializeBloomThreads(List<String> chunk) {
		super();
		this.chunk=chunk;
	}
	
	
	
	public static void setAnalysisPhaseSem(Semaphore analysisPhaseSem) {
		InitializeBloomThreads.analysisPhaseSem = analysisPhaseSem;
	}



	public static void setBloomFilter(BloomFilter<String>  myfilter){
		InitializeBloomThreads.filter = myfilter;
		
	}

   
	@Override
	public void run() {
		InitializeBloomThreads.filter.initializeMap(chunk);
			analysisPhaseSem.release();
	}
	

}

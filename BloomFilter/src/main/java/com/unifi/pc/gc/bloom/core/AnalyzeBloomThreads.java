package com.unifi.pc.gc.bloom.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class AnalyzeBloomThreads implements Callable<List<Integer>> {	
	private static BloomFilter  filter;
	private List chunk;
	private static Semaphore reportPhaseSem;
	private int rejectedNumber;
	private int admittedNumber;
	

	public AnalyzeBloomThreads(List chunk) {
		super();
		this.chunk=chunk;
		this.rejectedNumber=0;
		this.admittedNumber=0;
	}

	
	public int getRejectedNumber() {
		return rejectedNumber;
	}



	public int getAdmittedNumber() {
		return admittedNumber;
	}


	public static void setReportPhaseSem(Semaphore reportPhaseSem) {
		AnalyzeBloomThreads.reportPhaseSem = reportPhaseSem;
	}


	public static void setBloomFilter(BloomFilter  myfilter){
		AnalyzeBloomThreads.filter = myfilter;		
	}
	
	
	

	@Override
	public List<Integer> call() {
		//viewFilter(filter.viewPositionsOfOne());
		int[] result=AnalyzeBloomThreads.filter.analyze(chunk);
		admittedNumber= result[0];
		rejectedNumber= result[1];
		List<Integer> rst= new ArrayList<>();
		rst.add(admittedNumber);
		rst.add(rejectedNumber);
		synchronized(this){
			reportPhaseSem.release();
		}
		return rst;
	}

}

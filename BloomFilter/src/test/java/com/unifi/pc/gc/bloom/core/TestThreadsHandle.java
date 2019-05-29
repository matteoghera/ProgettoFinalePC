package com.unifi.pc.gc.bloom.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

public class TestThreadsHandle implements Callable<Integer> {
	

	private ParallelBloomFilter myParallelFilter;
	private int maxNumberThreads=0;

	public TestThreadsHandle(ParallelBloomFilter myParallelFilter) {
		this.myParallelFilter=myParallelFilter;
		
	}

	@Override
	public Integer call() throws Exception {
		ThreadPoolExecutor myThreadExecutor=(ThreadPoolExecutor) myParallelFilter.getMyThreads();
		/*
		 * Inserisco il ciclo for perchè l'esecuzione degli ultimi task è più veloce dell'esecuzione delle istruzioni:
		 * 
		 * 		if(numberThreads>maxNumberThreads) {
					maxNumberThreads=numberThreads;
				}
				
			Quindi gli ultimi valori assunti dalla variabile numberThreads li perdo.
			
			Esiste un modo più elegante per ottenere lo stesso risultato?
			
		 */
		for(int i=0; i<10; i++) { 
			do{
				int numberThreads=(int)myThreadExecutor.getCompletedTaskCount();
				if(numberThreads>maxNumberThreads) {
					maxNumberThreads=numberThreads;
				}
			}while(!myThreadExecutor.isTerminated());
		}
		return maxNumberThreads;
	}

}

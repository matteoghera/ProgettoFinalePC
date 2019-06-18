package com.unifi.pc.gc.bloom.core;

import java.util.Iterator;
import java.util.List;

import com.unifi.pc.gc.bloom.utility.HashFunction;

public class BloomFilter<T> {
	//filter
	private boolean[] map;
	//hash functions
	private List<HashFunction<T>> myHashFunctionList;

//constructor
	public BloomFilter(int n, List<HashFunction<T>> myHashFunctionList){
		this.myHashFunctionList=myHashFunctionList;
		map=new boolean[n];
		for(int i=0; i<n; i++) {
			map[i]=false;
		}
	}
	

	//compute the map, scrolling the sample
	public void initializeMap(List<T> sample) {
		Iterator<T> sampleIterator = sample.iterator();
		while (sampleIterator.hasNext()) {
			T element = sampleIterator.next();
			initializeMap(element);
		}
	}

	public void initializeMap(T element) {
		Iterator<HashFunction<T>> hashFunctionIterator = myHashFunctionList.iterator();
		while (hashFunctionIterator.hasNext()) {
			HashFunction<T> currentHashFunction = hashFunctionIterator.next();
			int hash=currentHashFunction.compute(element);
			map[hash] = true;
		}
	}

//control if an element is in the sample
	public boolean check(T element) {
		boolean result=true;
		Iterator<HashFunction<T>> hashFunctionIterator=myHashFunctionList.iterator();
		while(hashFunctionIterator.hasNext()) {
			HashFunction<T> currentHashFunction=hashFunctionIterator.next();
			result=result && map[currentHashFunction.compute(element)];
		}
		return result;
	}
	
// count the number of elements of the sample
	public int getNumberOfOnes() {
		int ones=0;
		for(int i=0; i<map.length; i++) {
			if(map[i]) {
				ones++;
			}
		}
		return ones;
	}

// save in an array the positions of ones
	public int[] viewPositionsOfOne() {
		int[] positionOfOnes=new int[getNumberOfOnes()];
		int i=0;
		for(int j=0; j<map.length; j++) {
			if(map[j]) {
				positionOfOnes[i]=j;
				i++;
			}
		}
		return positionOfOnes;
	}
	
// cont the admitted elements and the rejected
	public int[] analyze(List<T> dataFlow) {
		int numberOfElementsAdmitted=0, numberOfElementsRejected=0;
		Iterator<T> dataFlowIterator=dataFlow.iterator();
		while(dataFlowIterator.hasNext()) {
			if(check(dataFlowIterator.next())) {
				numberOfElementsAdmitted++;
			} else {
				numberOfElementsRejected++;
			}
		}
		
		int[] numbers= {numberOfElementsAdmitted, numberOfElementsRejected};
		return numbers;
	}
}

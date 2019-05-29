package com.unifi.pc.gc.bloom.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Writable;

import com.unifi.pc.gc.bloom.utility.HashFunction;
import com.unifi.pc.gc.bloom.utility.StringHashFunction;

public class BloomFilter<T> implements Writable{
	private boolean[] map;
	private List<HashFunction<T>> myHashFunctionList;
	
	private int dimensionOfSample;
	private int numberOfElementsAnalyzed;

	public BloomFilter() { 
		dimensionOfSample=0;
		numberOfElementsAnalyzed=0;
	}
	
	public BloomFilter(int n, List<HashFunction<T>> myHashFunctionList){
		this.myHashFunctionList=myHashFunctionList;
		map=new boolean[n];
		for(int i=0; i<n; i++) {
			map[i]=false;
		}
		dimensionOfSample=0;
		numberOfElementsAnalyzed=0;
	}
	


	public void initializeMap(List<T> sample) {
		Iterator<T> sampleIterator = sample.iterator();
		while (sampleIterator.hasNext()) {
			T element = sampleIterator.next();
			initializeMap(element);
		}
	}

	public void initializeMap(T element) {
		dimensionOfSample++;
		Iterator<HashFunction<T>> hashFunctionIterator = myHashFunctionList.iterator();
		while (hashFunctionIterator.hasNext()) {
			HashFunction<T> currentHashFunction = hashFunctionIterator.next();
			int hash=currentHashFunction.compute(element);
			//System.out.println("Stringa: "+element.toString()+", valore hash: "+ hash);
			map[hash] = true;
		}
	}
	public boolean check(T element) {
		numberOfElementsAnalyzed++;
		boolean result=true;
		Iterator<HashFunction<T>> hashFunctionIterator=myHashFunctionList.iterator();
		while(hashFunctionIterator.hasNext()) {
			HashFunction<T> currentHashFunction=hashFunctionIterator.next();
			result=result && map[currentHashFunction.compute(element)];
		}
		return result;
	}
	
	public int getNumberOfOnes() {
		int ones=0;
		for(int i=0; i<map.length; i++) {
			if(map[i]) {
				ones++;
			}
		}
		return ones;
	}

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
	
	
	public int[] analyze(List<T> dataFlow) {
		Iterator<T> dataFlowIterator=dataFlow.iterator();
		int numberOfElementsAdmitted=0, numberOfElementsRejected=0;
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



	public void union(BloomFilter<T> otherBloomFilter) {
		boolean[] otherMap=otherBloomFilter.map;
		for(int i=0; i<map.length; i++) {
			map[i]=map[i]||otherMap[i];
		}
		this.numberOfElementsAnalyzed+=otherBloomFilter.numberOfElementsAnalyzed;
		this.dimensionOfSample+=otherBloomFilter.dimensionOfSample;
	}
	


	public int getNumberOfElementsAnalyzed() {
		return numberOfElementsAnalyzed;
	}



	@Override
	public String toString() {
		int dimensionOfMap=map.length;
		int numberOfFunctions=myHashFunctionList.size();
		int numberOfOnes=getNumberOfOnes();
		
		
		String result="Execution Results:\n";
		result=result+"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
		result=result+"Number of functions utilized: "+numberOfFunctions+"\t"+"Dimension of sample: "+ dimensionOfSample+"\t"+"Dimension of vector of bits: "+dimensionOfMap+"\n";
		result=result+"Number of elements analyzed: "+numberOfElementsAnalyzed+"\t"+ "Number of ones: "+numberOfOnes+"\n\n";

		return result;
	}



	@Override
	public void readFields(DataInput input) throws IOException {
		int n=input.readInt();
		int numberOfHash=input.readInt();
		
		map=new boolean [n];
		for(int i=0; i<n; i++) {
			map[i]=input.readBoolean();
		}
		System.out.print(n+" ");
		myHashFunctionList=new ArrayList<>();
		for (int i=0; i<numberOfHash; i++) {
			HashFunction<T> myHash=new HashFunction<T>();
			myHash.readFields(input);
			myHashFunctionList.add(myHash);
		}
		numberOfElementsAnalyzed=input.readInt();
		dimensionOfSample=input.readInt();
	}



	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(map.length);
		output.writeInt(myHashFunctionList.size());
		for(int i=0; i<map.length; i++) {
			output.writeBoolean(map[i]);
		}
		Iterator <HashFunction<T>> iteratorHash=myHashFunctionList.iterator();
		while(iteratorHash.hasNext()) {
			iteratorHash.next().write(output);
		}
		output.writeInt(numberOfElementsAnalyzed);
		output.writeInt(dimensionOfSample);
	}
}

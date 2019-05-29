package com.unifi.pc.gc.bloom.core;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.unifi.pc.gc.bloom.utility.HashFunction;
import com.unifi.pc.gc.bloom.utility.StringHashFunction;

public class BloomFilterTest {
	BloomFilter<Integer> myBloomFilter;
	List<Integer>sample;
	List<HashFunction<Integer>> myHashFunctionList;
	int n=10;
	//m dimension of sample 
	

	@Before
	public void setUp() throws Exception {
		sample=new ArrayList<Integer>();
		myHashFunctionList=new ArrayList<HashFunction<Integer>>();
	}

	@Test
	public void testInitializeMapWithAnHashFuction() {
		oneHashFunctionTestsSupport();
		myBloomFilter.initializeMap(sample);
		assertEquals(3, myBloomFilter.getNumberOfOnes());
		
	}

	@Test
	public void testViewPositionOfOneWithAnHashFunction() {
		oneHashFunctionTestsSupport();
		myBloomFilter.initializeMap(sample);
		int[] result= {1,7,9};
		assertArrayEquals(result, myBloomFilter.viewPositionsOfOne());
	}
	
	@Test
	public void testInitializeMapWithMoreHashFunctions() {
		moreHashFunctionsTestsSupport();
		myBloomFilter.initializeMap(sample);
		assertEquals(7, myBloomFilter.getNumberOfOnes());
	}
	
	@Test
	public void testViewPositionOfOneWithMoreHashFunctions() {
		moreHashFunctionsTestsSupport();
		myBloomFilter.initializeMap(sample);
		int[] result= {0,1,2,3,5,7,9};
		assertArrayEquals(result, myBloomFilter.viewPositionsOfOne());
	}
	
	@Test
	public void testCheckWithElementInTheSample() {
		boolean result = checkElement(3569);
		assertTrue(result);
		result = checkElement(7761);
		assertTrue(result);
	}
	
	@Test
	public void testCheckWithForeignElement() {
		boolean result = !checkElement(4503);
		assertTrue(result);
	}
	
	@Test
	public void testAnalyze() {
		int[] setElements= {345,654, 987, 567, 123, 456, 789, 978, 125, 145, 176, 890, 910};
		List<Integer> dataFlow=createSet(setElements);
		int[] sampleElements= {987,456, 125, 890};
		sample=createSet(sampleElements);
		
		List<HashFunction<Integer>>myHashFunctionList=new ArrayList<HashFunction<Integer>>();
		myHashFunctionList.add(new HashFunction<>(10));
		myBloomFilter=new BloomFilter<>(10, myHashFunctionList);
		
		myBloomFilter.initializeMap(sample);
		int[] numbers=myBloomFilter.analyze(dataFlow);
		
		int numberOfElementsAdmitted=numbers[0];
		int numberOfElementsRejected=numbers[1];
		assertEquals(9, numberOfElementsAdmitted);
		assertEquals(4,numberOfElementsRejected);
	}
	
	@Test
	public void testUnion() {
		oneHashFunctionTestsSupport();
		myBloomFilter.initializeMap(sample);
		
		int[] elements= {4344, 1711};
		List<Integer> secondSample=createSet(elements);
		BloomFilter<Integer> secondBloomFilter=new BloomFilter<Integer>(n, myHashFunctionList);
		secondBloomFilter.initializeMap(secondSample);
		
		myBloomFilter.union(secondBloomFilter);
		int[] result= {1,4,7,9};
		assertArrayEquals(result, myBloomFilter.viewPositionsOfOne());
	}
	
	private List<Integer> createSet(int[] elements) {
		List<Integer> mySet=new ArrayList<>();
		for(int i=0; i<elements.length; i++) {
			mySet.add(elements[i]);
		}
		return mySet;
	}

	private boolean checkElement(int element) {
		moreHashFunctionsTestsSupport();
		myBloomFilter.initializeMap(sample);
		boolean result=myBloomFilter.check(element);
		return result;
	}

	private void moreHashFunctionsTestsSupport() {
		myHashFunctionList.add(new StringHashFunction<Integer>(n));
		myHashFunctionList.add(new HashFunction<Integer>(n));
		myBloomFilter=new BloomFilter<Integer>(n, myHashFunctionList);
		int[] elements = {3569,6787,4321, 7761};
		sample=createSet(elements);
	}

	private void oneHashFunctionTestsSupport() {
		myHashFunctionList.add(new HashFunction<Integer>(n));
		myBloomFilter=new BloomFilter<Integer>(n, myHashFunctionList);
		int[] elements = {3569,6787,4321};
		sample=createSet(elements);
	}
		

}

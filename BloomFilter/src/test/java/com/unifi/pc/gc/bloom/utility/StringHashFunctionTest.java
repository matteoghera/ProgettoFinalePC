package com.unifi.pc.gc.bloom.utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringHashFunctionTest {

	@Test
	public void testComputeWithInteger() {
		HashFunction<Integer> myHashFunction=new StringHashFunction<Integer>(10);
		assertEquals(5, myHashFunction.compute(new Integer(3569)));
	}
	
	@Test
	public void testComputeWithString() {
		HashFunction<String> myHashFunction=new StringHashFunction<String>(10);
		assertEquals(7, myHashFunction.compute(new String("copossolkhantrintsiseresconacr")));
	}

}

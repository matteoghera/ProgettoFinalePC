package com.unifi.pc.gc.bloom.utility;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleHashFunctionTest {
	HashFunction<Integer> myHashFunction;

	@Before
	public void setUp() throws Exception {
		myHashFunction=new HashFunction<Integer>(10);
	}

	@Test
	public void testCompute() {
		assertEquals(9, myHashFunction.compute(new Integer(3569)));
	}

}

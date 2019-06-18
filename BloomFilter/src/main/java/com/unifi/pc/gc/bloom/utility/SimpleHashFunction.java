package com.unifi.pc.gc.bloom.utility;

public class SimpleHashFunction<T> implements HashFunction<T> {
	private int n;
	
	public SimpleHashFunction(int n) {
		this.n=n;
	}

	@Override
	public int compute(T element) {
		return element.hashCode()%n;
	}

}

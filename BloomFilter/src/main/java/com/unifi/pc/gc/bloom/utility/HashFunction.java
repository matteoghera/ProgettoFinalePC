package com.unifi.pc.gc.bloom.utility;

public class HashFunction<T> {
	protected int n;
	
	public HashFunction(int n) {
		this.n=n;
	}

	public int compute(T element) {
		return element.hashCode()%n;
	}

}

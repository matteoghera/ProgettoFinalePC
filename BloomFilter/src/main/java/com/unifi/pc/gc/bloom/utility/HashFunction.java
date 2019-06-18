package com.unifi.pc.gc.bloom.utility;

public interface HashFunction<T> {
	public int compute(T element);
}

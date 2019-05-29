package com.unifi.pc.gc.bloom.utility;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.Writable;

public class HashFunction<T> implements Writable {
	protected int n;
	
	public HashFunction(int n) {
		this.n=n;
	}

	public HashFunction() {
		n=0;
	}

	public int compute(T element) {
		return element.hashCode()%n;
	}
	
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(n);
		
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		input.readInt();
		
	}

}

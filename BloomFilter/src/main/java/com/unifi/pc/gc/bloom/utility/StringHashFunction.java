package com.unifi.pc.gc.bloom.utility;

public class StringHashFunction<T> implements HashFunction<T> {
	private int n;

	public StringHashFunction(int n) {
		this.n=n;
	}

	@Override
	public int compute(T element) {
		String myElementInString= element.toString();
		return hashString(myElementInString, 31);
		
	}
	
	private int hashString(String s, int R) {
		int hash = 0;
		for (int i = 0; i < s.length(); i++)
		    hash = (R * hash + s.charAt(i)) % n;
		return hash;
	}

}

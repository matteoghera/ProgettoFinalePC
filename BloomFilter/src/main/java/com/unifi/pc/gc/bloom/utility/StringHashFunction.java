package com.unifi.pc.gc.bloom.utility;

public class StringHashFunction<T> extends HashFunction<T> {

	public StringHashFunction(int n) {
		super(n);
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

package com.sandwich.util;

public class Counter {

	private int count;
	
	public Counter(){
		this(0);
	}
	
	public Counter(int count){
		this.count = count;
	}
	
	public void count(){
		count++;
	};
	
	public int getCount(){
		return count;
	}
	
}

package com.sandwich.util;

public class Counter {

	private long count;
	
	public Counter(){
		this(0);
	}
	
	public Counter(long count){
		this.count = count;
	}
	
	public void count(){
		count++;
	};
	
	public long getCount(){
		return count;
	}
	
	@Override
	public String toString() {
		return "Counter [count=" + count + "]";
	}
	
}

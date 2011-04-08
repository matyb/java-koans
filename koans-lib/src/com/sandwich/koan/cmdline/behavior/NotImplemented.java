package com.sandwich.koan.cmdline.behavior;


public class NotImplemented implements ArgumentBehavior{

	public void run(String value) {
		throw new UnsupportedOperationException();
	}
	
}

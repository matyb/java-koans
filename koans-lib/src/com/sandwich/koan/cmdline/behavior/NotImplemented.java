package com.sandwich.koan.cmdline.behavior;


public class NotImplemented implements ArgumentBehavior{

	@Override
	public void run(String value) {
		throw new UnsupportedOperationException();
	}
	
}

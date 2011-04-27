package com.sandwich.koan.cmdline.behavior;


public class NotImplemented extends AbstractArgumentBehavior{

	public void run(String value) {
		throw new UnsupportedOperationException();
	}
	
}

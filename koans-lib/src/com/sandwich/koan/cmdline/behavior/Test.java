package com.sandwich.koan.cmdline.behavior;

public class Test implements ArgumentBehavior{

	@Override
	public void run(String value) {
		System.out.println("Test");
	}
	
}

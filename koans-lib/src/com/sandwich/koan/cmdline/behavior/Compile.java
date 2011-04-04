package com.sandwich.koan.cmdline.behavior;

public class Compile implements ArgumentBehavior {

	@Override
	public void run(String value) {
		System.out.println("Compile");
	}
	
}

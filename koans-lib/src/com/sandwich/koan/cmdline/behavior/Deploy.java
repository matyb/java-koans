package com.sandwich.koan.cmdline.behavior;

public class Deploy implements ArgumentBehavior {

	@Override
	public void run(String value) {
		System.out.println("Deploy");
	}
	
}

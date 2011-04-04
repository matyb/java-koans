package com.sandwich.koan.cmdline.behavior;

public class Commit implements ArgumentBehavior {
	
	@Override
	public void run(String value) {
		System.out.println("Commit");
	}
	
}

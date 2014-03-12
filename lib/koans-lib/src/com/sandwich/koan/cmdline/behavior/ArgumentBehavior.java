package com.sandwich.koan.cmdline.behavior;


public interface ArgumentBehavior {

	void run(String... values) throws Exception;
	String getErrorMessage();
	String getSuccessMessage();
	
}

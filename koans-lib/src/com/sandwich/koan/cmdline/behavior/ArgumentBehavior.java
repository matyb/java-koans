package com.sandwich.koan.cmdline.behavior;


public interface ArgumentBehavior {

	void run(String value) throws Exception;
	String getErrorMessage();
	String getSuccessMessage();
	
}

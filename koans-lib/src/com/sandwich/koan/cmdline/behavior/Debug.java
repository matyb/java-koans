package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.constant.KoanConstants;

public class Debug implements ArgumentBehavior{

	public void run(String arg){
		KoanConstants.DEBUG = KoanConstants.DEBUG || 
			"true".equalsIgnoreCase(arg) ||
				arg == null ||
					arg.trim().length() == 0;
	}
	
}

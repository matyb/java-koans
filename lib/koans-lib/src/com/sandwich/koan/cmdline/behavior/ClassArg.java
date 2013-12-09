package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class ClassArg extends AbstractArgumentBehavior {

	public void run(String...koanSuiteClassName) {
		if(koanSuiteClassName != null && 
				koanSuiteClassName.length > 0 &&
				koanSuiteClassName[0] != null && 
				koanSuiteClassName[0].trim().length() != 0){
			PathToEnlightenment.filterBySuite(koanSuiteClassName[0]);
		}
	}
	
}

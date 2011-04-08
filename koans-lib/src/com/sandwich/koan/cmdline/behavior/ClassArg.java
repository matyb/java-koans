package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class ClassArg implements ArgumentBehavior {

	public void run(String koanSuiteClassName) {
		if(koanSuiteClassName != null && koanSuiteClassName.trim().length() != 0){
			PathToEnlightenment.stagePathToEnlightenment(koanSuiteClassName);
		}
	}
	
}

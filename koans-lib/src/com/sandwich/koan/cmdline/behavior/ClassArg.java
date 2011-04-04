package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class ClassArg implements ArgumentBehavior {

	@Override
	public void run(String koanSuiteClassName) {
		if(koanSuiteClassName != null && !koanSuiteClassName.trim().isEmpty()){
			PathToEnlightenment.stagePathToEnlightenment(koanSuiteClassName);
		}
	}
	
}

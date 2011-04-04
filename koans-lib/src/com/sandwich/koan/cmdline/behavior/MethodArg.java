package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class MethodArg implements ArgumentBehavior {

	@Override
	public void run(String koanName) {
		if(koanName != null && !koanName.trim().isEmpty()){
			PathToEnlightenment.removeAllKoanMethodsExcept(koanName);
		}
	}
	
}

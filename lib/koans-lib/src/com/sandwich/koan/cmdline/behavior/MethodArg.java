package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class MethodArg extends AbstractArgumentBehavior {

	public void run(String...koanName) {
		if(koanName != null && 
				koanName.length > 0 &&
				koanName[0] != null && 
				koanName[0].trim().length() != 0){
			PathToEnlightenment.filterByKoan(koanName[0]);
		}
	}
	
}

package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.path.PathToEnlightenment;

public class MethodArg extends AbstractArgumentBehavior {

	public void run(String koanName) {
		if(koanName != null && koanName.trim().length() != 0){
			PathToEnlightenment.filterByKoan(koanName);
		}
	}
	
}

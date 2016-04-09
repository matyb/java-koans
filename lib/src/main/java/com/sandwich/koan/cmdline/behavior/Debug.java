package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.ApplicationSettings;

public class Debug extends AbstractArgumentBehavior{

	public void run(String... args){
		ApplicationSettings.setDebug(ApplicationSettings.isDebug() || 
			"true".equalsIgnoreCase(args[0]) ||
				args[0] == null ||
					args[0].trim().length() == 0);
	}
	
}

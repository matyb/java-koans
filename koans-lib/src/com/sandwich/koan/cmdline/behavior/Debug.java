package com.sandwich.koan.cmdline.behavior;

import com.sandwich.koan.ApplicationSettings;

public class Debug extends AbstractArgumentBehavior{

	public void run(String arg){
		ApplicationSettings.setDebug(ApplicationSettings.isDebug() || 
			"true".equalsIgnoreCase(arg) ||
				arg == null ||
					arg.trim().length() == 0);
	}
	
}

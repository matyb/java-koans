package com.sandwich.util;

import com.sandwich.koan.util.ApplicationUtils;

public class ConsoleUtils {

	private static final int NUMBER_OF_LINES_TO_CLEAR_CONSOLE = 80;
	
	public static void clearConsole(){
		for(int i = 0; i < NUMBER_OF_LINES_TO_CLEAR_CONSOLE; i++){
			ApplicationUtils.getPresenter().displayMessage(" ");
		}
	}
	
}

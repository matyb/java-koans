package com.sandwich.util;

public class ConsoleUtils {

	private static final int NUMBER_OF_LINES_TO_CLEAR_CONSOLE = 80;
	
	public static void clearConsole(){
		for(int i = 0; i < NUMBER_OF_LINES_TO_CLEAR_CONSOLE; i++){
			System.out.println();
		}
	}
	
}

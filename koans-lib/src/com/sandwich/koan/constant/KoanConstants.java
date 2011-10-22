package com.sandwich.koan.constant;

import com.sandwich.util.Strings;

public abstract class KoanConstants {

	private KoanConstants(){}
	
	public static final String  __					= Strings.getMessage("__");
	
	public static final String  EOL					= System.getProperty("line.separator");
	public static final String  EOLS				= "[\n\r"+EOL+"]";
	
	public static final String  PERIOD 				= ".";
	public static final String  EXPECTATION_LEFT_ARG= "has expectation as wrong argument!";
	public static final String  EXPECTED_LEFT 		=  ":<";
	public static final String  EXPECTED_RIGHT 		= ">";
	public static final String  LINE_NO_START		= ".java:";
	public static final String  LINE_NO_END 		= ")";
	
	public static final String 	COMPLETE_CHAR 		= "X";
	public static final String 	INCOMPLETE_CHAR		= "-";
	
	public static final int		PROGRESS_BAR_WIDTH	= 50;
	public static final String 	PROGRESS_BAR_START	= "[";
	public static final String 	PROGRESS_BAR_END	= "]";
	
	public static final String  XML_PARAMETER_START = "${";
	public static final String  XML_PARAMETER_END 	= "}";
	
}

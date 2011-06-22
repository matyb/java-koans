package com.sandwich.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	
	public static String convertToPopulatedStackTraceString(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		if(t == null){
			return "";
		}
		t.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
	
}

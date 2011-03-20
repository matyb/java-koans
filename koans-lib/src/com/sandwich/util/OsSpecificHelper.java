package com.sandwich.util;

import static com.sandwich.koan.KoanConstants.EOLS;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.sandwich.koan.KoanConstants;

public class OsSpecificHelper {

	public static String getOriginalLineNumber(Throwable t, Class<?> failingSuite){
		StringWriter stringWriter = new StringWriter();
		if(t == null){
			return null;
		}
		t.printStackTrace(new PrintWriter(stringWriter));
		String[] lines = stringWriter.toString().split(EOLS);
		for(int i = lines.length - 1; i >= 0; --i){
			String line = lines[i];
			if(line.contains(failingSuite.getName())){
				return line.substring(
						line.indexOf(KoanConstants.LINE_NO_START)+KoanConstants.LINE_NO_START.length(),
						line.lastIndexOf(KoanConstants.LINE_NO_END));
			}
		}
		return null;
	}
	
}

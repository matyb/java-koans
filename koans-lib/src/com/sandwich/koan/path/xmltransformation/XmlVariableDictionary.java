package com.sandwich.koan.path.xmltransformation;

import com.sandwich.koan.constant.KoanConstants;

public class XmlVariableDictionary {

	public static final String METHOD_NAME 	= wrapParam("method_name");
	public static final String FILE_NAME 	= wrapParam("file_name");
	public static final String FILE_PATH 	= wrapParam("file_path");
	
	private static String wrapParam(String param) {
		return new StringBuilder(KoanConstants.XML_PARAMETER_START)
			.append(param).append(KoanConstants.XML_PARAMETER_END).toString();
	}
}

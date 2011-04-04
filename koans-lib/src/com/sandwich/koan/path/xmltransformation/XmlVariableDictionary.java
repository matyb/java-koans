package com.sandwich.koan.path.xmltransformation;

import com.sandwich.koan.constant.KoanConstants;

public class XmlVariableDictionary {

	public static final String METHOD_NAME = new StringBuilder(KoanConstants.XML_PARAMETER_START)
		.append("method_name").append(KoanConstants.XML_PARAMETER_END).toString();
	public static final String FILE_NAME = new StringBuilder(KoanConstants.XML_PARAMETER_START)
		.append("file_name").append(KoanConstants.XML_PARAMETER_END).toString();
	public static final String FILE_PATH = new StringBuilder(KoanConstants.XML_PARAMETER_START)
		.append("file_path").append(KoanConstants.XML_PARAMETER_END).toString();
}

package com.sandwich.koan.path;

import java.io.File;
import java.lang.reflect.Method;

import com.sandwich.koan.KoanConstants;

public class XmlVariableInjector {

	private final String lesson;
	private final String methodName;
	private final String suiteName;
	private final String suitePath;
	
	
	public XmlVariableInjector(String lesson, Method koanMethod){
		if(lesson == null){
			throw new IllegalArgumentException("lesson may not be null");
		}
		if(koanMethod == null){
			throw new IllegalArgumentException("koanMethod may not be null");
		}
		this.lesson = lesson;
		methodName = koanMethod.getName();
		suiteName = koanMethod.getDeclaringClass().getSimpleName();
		suitePath = new File(KoanConstants.PATH_XML+koanMethod.getDeclaringClass().getName().replace('.', '/')).getAbsolutePath()
			.replace(koanMethod.getDeclaringClass().getSimpleName(), "");
	}
	
	public String injectLessonVariables() {
		if(lesson.length() == 0 || 
				lesson.indexOf(KoanConstants.XML_PARAMETER_START) < 0){
			return lesson;
		}
		StringBuffer sb = new StringBuffer(lesson);
		replace(sb, XmlVariableDictionary.FILE_NAME, suiteName);
		replace(sb, XmlVariableDictionary.FILE_PATH, suitePath);
		replace(sb, XmlVariableDictionary.METHOD_NAME, methodName);
		return sb.toString();
	}

	private void replace(StringBuffer sb, String keyword, String value) {
		int indexOfKeyword = sb.indexOf(keyword);
		if(indexOfKeyword < 0){
			return;
		}
		sb.replace(indexOfKeyword, indexOfKeyword+keyword.length(), value);
	}

}

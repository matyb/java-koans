package com.sandwich.koan.path.xmltransformation;

import java.lang.reflect.Method;

import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.util.Strings;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.filecompiler.FileCompiler;

public class RbVariableInjector {

	private final String lesson;
	private final String methodName;
	private final String suiteName;
	private final String suitePath;
	
	
	public RbVariableInjector(String lesson, Method koanMethod){
		if(koanMethod == null){
			throw new IllegalArgumentException("koanMethod may not be null");
		}
		Class<?> declaringClass = koanMethod.getDeclaringClass();
		if(lesson == null){
			lesson = Strings.getMessage(declaringClass.getCanonicalName() + '.' + koanMethod.getName());
			if(Strings.wasNotFound(lesson)){
				lesson = null;
			}
		}
		this.lesson = lesson;
		methodName = koanMethod.getName();
		suiteName = declaringClass.getSimpleName();
		String path = FileCompiler.getSourceFileFromClass(DirectoryManager.getSourceDir(), declaringClass.getName()).getAbsolutePath();
		suitePath = path.substring(0, path.lastIndexOf(DirectoryManager.FILESYSTEM_SEPARATOR) + 1);
	}
	
	public String injectLessonVariables() {
		if(lesson == null || lesson.length() == 0 || 
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

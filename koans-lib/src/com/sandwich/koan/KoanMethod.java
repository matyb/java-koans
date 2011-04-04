package com.sandwich.koan;

import java.lang.reflect.Method;

import com.sandwich.koan.path.xmltransformation.XmlVariableInjector;

public class KoanMethod {
	
	private final transient Method method;
	private final String lesson;
	private final boolean displayIncompleteException;
	
	public KoanMethod(String lesson, Method method){
		this(lesson,method,true);
	}
	
	public KoanMethod(String lesson, Method method, boolean displayIncompleteException){
		if(method == null){
			throw new IllegalArgumentException("method may not be null");
		}
		this.method = method;
		this.lesson = new XmlVariableInjector(lesson, method).injectLessonVariables();
		this.displayIncompleteException = displayIncompleteException;
	}

	public String getLesson() {
		return lesson;
	}

	public Method getMethod() {
		return method;
	}
	
	public boolean displayIncompleteException() {
		return displayIncompleteException;
	}
	
	@Override public String toString(){
		return "{"+getMethod().getName()
				+" : "+ (lesson.length() > 20 ? lesson.substring(0, 20) + "..." : lesson)+"}";
	}
	
	@Override public boolean equals(Object o){
		return o instanceof KoanMethod && 
			method.equals(((KoanMethod)o).method) &&
			lesson.equals(((KoanMethod)o).lesson);
	}
}

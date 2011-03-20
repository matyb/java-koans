package com.sandwich.koan;

import java.lang.reflect.Method;

public class KoanMethod {
	private final transient Method method;
	private final String lesson;
	public KoanMethod(String lesson, Method method){
		this.lesson = lesson;
		this.method = method;
	}
	public String getLesson(){return lesson;}
	public Method getMethod(){return method;}
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

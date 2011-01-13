package com.sandwich.koans;

import java.lang.reflect.Method;

public class KoansResult {

	int numberPassing = -1;
	Class<?> failingCase;
	Method failingMethod;
	String message;
	
	public KoansResult() {
		this(-1, null, null);
	}
	
	public KoansResult(int numberPassing, Class<?> failingCase, Method failingMethod){
		this.numberPassing = numberPassing;
		this.failingCase = failingCase;
		this.failingMethod = failingMethod;
	}

	public KoansResult(int numberPassing,
			Class<?> failingCase, Method failingMethod,
			String message) {
		this(numberPassing, failingCase, failingMethod);
		this.message = message;
	}

	public int getNumberPassing() {
		return numberPassing;
	}

	public Class<?> getFailingCase() {
		return failingCase;
	}

	public Method getFailingMethod() {
		return failingMethod;
	}
	
	public String getMessage() {
		return message;
	}

	public boolean isAllKoansSuccessful() {
		return failingCase == null && failingMethod == null;
	}

	@Override
	public String toString() {
		return "KoansResult [numberPassing=" + numberPassing + ", failingCase="
				+ failingCase + ", failingMethod=" + failingMethod + "]";
	}
	
}

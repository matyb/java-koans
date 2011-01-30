package com.sandwich.koan;

import java.lang.reflect.Method;
import java.util.List;

public class KoansResult {

	int numberPassing = -1;
	int totalNumberOfKoanMethods = -1;
	Class<?> failingCase;
	List<Class<?>> passingCases;
	List<Class<?>> remainingCases;
	Method failingMethod;
	String message;
	String lineNumber;

	public KoansResult(int numberPassing, int totalNumberOfKoanMethods,
			Class<?> failingCase, Method failingMethod,
			String message, String lineNumber, List<Class<?>> passingCases, List<Class<?>> remainingCases) {
		this.numberPassing = numberPassing;
		this.failingCase = failingCase;
		this.failingMethod = failingMethod;
		this.totalNumberOfKoanMethods = totalNumberOfKoanMethods;
		this.lineNumber = lineNumber;
		this.message = message;
		this.passingCases = passingCases;
		this.remainingCases = remainingCases;
	}

	public int getNumberPassing() {
		return numberPassing;
	}
	
	public int getTotalNumberOfKoans(){
		return totalNumberOfKoanMethods;
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

	public List<Class<?>> getPassingSuites() {
		return passingCases;
	}

	public List<Class<?>> getRemainingSuites() {
		return remainingCases;
	}

	public String getLineNumber(){
		return lineNumber;
	}
	
	public boolean isAllKoansSuccessful() {
		return failingCase == null && failingMethod == null;
	}

	@Override
	public String toString() {
		return "KoansResult [numberPassing=" + numberPassing
				+ ", totalNumberOfKoanMethods=" + totalNumberOfKoanMethods
				+ ", failingCase=" + failingCase + ", passingCases="
				+ passingCases + ", remainingCases=" + remainingCases
				+ ", failingMethod=" + failingMethod + ", message=" + message
				+ ", lineNumber=" + lineNumber + "]";
	}	
	
}

package com.sandwich.koan;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class KoanResult {

	String level;
	int numberPassing = -1;
	int totalNumberOfKoanMethods = -1;
	Class<?> failingCase;
	List<Class<?>> passingCases;
	List<Class<?>> remainingCases;
	Method failingMethod;
	String message;
	String lineNumber;

	@SuppressWarnings("unchecked") // may not be mutated - 0 elements in emptyList()
	public KoanResult(String level, int numberPassing, int totalNumberOfKoanMethods,
			Class<?> failingCase, Method failingMethod, String message, String lineNumber, 
			List<Class<?>> passingCases, List<Class<?>> remainingCases) {
		this.level = level;
		this.numberPassing = numberPassing;
		this.failingCase = failingCase;
		this.failingMethod = failingMethod;
		this.totalNumberOfKoanMethods = totalNumberOfKoanMethods;
		this.lineNumber = lineNumber;
		this.message = message;
		this.passingCases = Collections
				.unmodifiableList(passingCases == null ? Collections.EMPTY_LIST : passingCases);
		this.remainingCases = Collections
				.unmodifiableList(remainingCases == null ? Collections.EMPTY_LIST : remainingCases);
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

	public String getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return "KoanResult [level=" + level + ", numberPassing="
				+ numberPassing + ", totalNumberOfKoanMethods="
				+ totalNumberOfKoanMethods + ", failingCase=" + failingCase
				+ ", passingCases=" + passingCases + ", remainingCases="
				+ remainingCases + ", failingMethod=" + failingMethod
				+ ", message=" + message + ", lineNumber=" + lineNumber + "]";
	}
	
	
}

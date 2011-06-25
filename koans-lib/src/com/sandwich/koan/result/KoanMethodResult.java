package com.sandwich.koan.result;

import com.sandwich.koan.KoanMethod;

public class KoanMethodResult {

	public final static KoanMethodResult PASSED = new KoanMethodResult(null,"PASSED","PASSED");
	
	private final String message;
	private final String lineNumber;
	private final KoanMethod failingMethod;	
	
	public KoanMethodResult(KoanMethod failingMethod, String message, String lineNumber){
		this.message = message;
		this.failingMethod = failingMethod;
		this.lineNumber = lineNumber;
	}

	public static KoanMethodResult getPassing() {
		return PASSED;
	}

	public String getMessage() {
		return message;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public KoanMethod getFailingMethod() {
		return failingMethod;
	}

	public boolean isPassed(){
		return this == PASSED;
	}
	
	@Override
	public String toString() {
		return "KoanMethodResult [message=" + message + ", lineNumber="
				+ lineNumber + ", failingMethod=" + failingMethod + "]";
	}
	
}

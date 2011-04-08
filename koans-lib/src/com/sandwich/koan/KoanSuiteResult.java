package com.sandwich.koan;

import java.util.Collections;
import java.util.List;

import com.sandwich.util.Builder;

public class KoanSuiteResult {

	private final KoanResultModel state;

	static class KoanResultModel {
		String level;
		int numberPassing = -1;
		int totalNumberOfKoanMethods = -1;
		Class<?> failingCase;
		KoanMethod failingMethod;
		List<Class<?>> passingCases;
		List<Class<?>> remainingCases;
		String message;
		String lineNumber;
		@Override
		public String toString() {
			return "KoanResult [level=" + level + ", numberPassing="
					+ numberPassing + ", totalNumberOfKoanMethods="
					+ totalNumberOfKoanMethods + ", failingCase=" + failingCase
					+ ", passingCases=" + passingCases + ", remainingCases="
					+ (remainingCases + ", failingMethod=" + failingMethod == null ? ""
							: failingMethod.getMethod() == null ? "" : failingMethod.getMethod().getName())
					+ ", message=" + message + ", lineNumber=" + lineNumber + "]";
		}
	}
	
	KoanSuiteResult(KoanResultModel state) {
		this.state = state;
	}

	public static class KoanResultBuilder implements Builder<KoanSuiteResult>{
		
		private final KoanResultModel state = new KoanResultModel();
		
		public KoanResultBuilder level(String level){
			state.level = level; return this;
		}
		public KoanResultBuilder numberPassing(int numberPassing){
			state.numberPassing = numberPassing; return this;
		}
		public KoanResultBuilder totalNumberOfKoanMethods(int totalNumberOfKoanMethods){
			state.totalNumberOfKoanMethods = totalNumberOfKoanMethods; return this;
		}
		public KoanResultBuilder failingCase(Class<?> failingCase){
			state.failingCase = failingCase; return this;
		}
		public KoanResultBuilder failingMethod(KoanMethod failingMethod){
			state.failingMethod = failingMethod; return this;
		}
		@SuppressWarnings("unchecked") // immutable and empty - its safe
		public KoanResultBuilder passingCases(List<Class<?>> passingCases){
			state.passingCases = passingCases == null ? 
					Collections.EMPTY_LIST : passingCases;
			return this;
		}
		@SuppressWarnings("unchecked") // immutable and empty - its safe
		public KoanResultBuilder remainingCases(List<Class<?>> remainingCases){
			state.remainingCases = remainingCases == null ? 
					Collections.EMPTY_LIST : remainingCases;
			return this;
		}
		public KoanResultBuilder message(String message){
			state.message = message; return this;
		}
		public KoanResultBuilder lineNumber(String lineNumber){
			state.lineNumber = lineNumber; return this;
		}
		public KoanSuiteResult build() {
			return new KoanSuiteResult(state);
		}
	}
	
	public int getNumberPassing() {
		return state.numberPassing;
	}
	
	public int getTotalNumberOfKoans(){
		return state.totalNumberOfKoanMethods;
	}

	public Class<?> getFailingCase() {
		return state.failingCase;
	}

	public KoanMethod getFailingMethod() {
		return state.failingMethod;
	}
	
	public String getMessage() {
		return state.message;
	}

	public List<Class<?>> getPassingSuites() {
		return state.passingCases;
	}

	public List<Class<?>> getRemainingSuites() {
		return state.remainingCases;
	}

	public String getLineNumber(){
		return state.lineNumber;
	}
	
	public boolean isAllKoansSuccessful() {
		return state.failingCase == null && state.failingMethod == null;
	}

	public String getLevel() {
		return state.level;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	public boolean displayIncompleteException() {
		return getFailingMethod() == null || getFailingMethod().displayIncompleteException();
	}
	
	
}

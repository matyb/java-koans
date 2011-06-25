package com.sandwich.koan.result;

import java.util.Collections;
import java.util.List;

import com.sandwich.koan.KoanMethod;
import com.sandwich.util.Builder;

public class KoanSuiteResult {

	private final KoanResultModel state;

	static class KoanResultModel {
		String level;
		int numberPassing = -1;
		int totalNumberOfKoanMethods = -1;
		List<String> passingCases;
		List<String> remainingCases;
		KoanMethodResult methodResult = KoanMethodResult.PASSED;
		@Override
		public String toString() {
			return "KoanResult [level=" + level + ", numberPassing="
					+ numberPassing + ", totalNumberOfKoanMethods="
					+ totalNumberOfKoanMethods
					+ ", passingCases=" + passingCases + ", remainingCases="
					+ remainingCases + ", failingMethod=" + methodResult.getFailingMethod()
					+ ", message=" + methodResult.getMessage() + ", lineNumber=" + methodResult.getLineNumber() + "]";
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
		@SuppressWarnings("unchecked") // immutable and empty - its safe
		public KoanResultBuilder passingCases(List<String> passingCases){
			state.passingCases = passingCases == null ? 
					Collections.EMPTY_LIST : passingCases;
			return this;
		}
		@SuppressWarnings("unchecked") // immutable and empty - its safe
		public KoanResultBuilder remainingCases(List<String> remainingCases){
			state.remainingCases = remainingCases == null ? 
					Collections.EMPTY_LIST : remainingCases;
			return this;
		}
		public KoanResultBuilder methodResult(KoanMethodResult methodResult){
			state.methodResult = methodResult == null ? KoanMethodResult.PASSED : methodResult; return this;
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

	public String getFailingCase() {
		return state.remainingCases.isEmpty() ? null : state.remainingCases.get(0);
	}

	public KoanMethod getFailingMethod() {
		return state.methodResult.getFailingMethod();
	}
	
	public String getMessage() {
		return state.methodResult.getMessage();
	}

	public List<String> getPassingSuites() {
		return state.passingCases;
	}

	public List<String> getRemainingSuites() {
		return state.remainingCases;
	}

	public String getLineNumber(){
		return state.methodResult.getLineNumber();
	}
	
	public boolean isAllKoansSuccessful() {
		return state.methodResult == KoanMethodResult.PASSED;
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

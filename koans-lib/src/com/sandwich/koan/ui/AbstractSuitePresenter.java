package com.sandwich.koan.ui;

import com.sandwich.koan.result.KoanSuiteResult;

public abstract class AbstractSuitePresenter implements SuitePresenter {

	public void displayResult(KoanSuiteResult result) {
		if (result.isAllKoansSuccessful()) {
			displayAllSuccess(result);
		} else {
			displayOneOrMoreFailure(result);
		}
		displayChart(result);
		displayPassingFailing(result);
		displayHeader(result);
	}
	
	abstract protected void displayHeader(KoanSuiteResult result);
	abstract protected void displayPassingFailing(KoanSuiteResult result);
	abstract protected void displayChart(KoanSuiteResult result);
	abstract protected void displayOneOrMoreFailure(KoanSuiteResult result);
	abstract protected void displayAllSuccess(KoanSuiteResult result);
}
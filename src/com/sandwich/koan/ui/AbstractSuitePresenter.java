package com.sandwich.koan.ui;

import com.sandwich.koan.KoanResult;

public abstract class AbstractSuitePresenter implements SuitePresenter {

	@Override
	public void displayResult(KoanResult result) {
		displayHeader(result);
		displayPassingFailing(result);
		displayChart(result);
		if (result.isAllKoansSuccessful()) {
			displayAllSuccess(result);
		} else {
			displayOneOrMoreFailure(result);
		}
	}

	abstract protected void displayHeader(KoanResult result);
	abstract protected void displayPassingFailing(KoanResult result);
	abstract protected void displayChart(KoanResult result);
	abstract protected void displayOneOrMoreFailure(KoanResult result);
	abstract protected void displayAllSuccess(KoanResult result);
}
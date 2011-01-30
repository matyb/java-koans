package com.sandwich.koan.runner.ui;

import com.sandwich.koan.KoansResult;

public abstract class AbstractSuitePresenter implements SuitePresenter {

	public AbstractSuitePresenter() {
		super();
	}

	@Override
	public void displayResult(KoansResult result) {
		displayHeader();
		displayPassingFailing(result);
		displayChart(result);
		if (result.isAllKoansSuccessful()) {
			displayAllSuccess(result);
		} else {
			displayOneOrMoreFailure(result);
		}
	}

	abstract protected void displayHeader();
	abstract protected void displayPassingFailing(KoansResult result);
	abstract protected void displayChart(KoansResult result);
	abstract protected void displayOneOrMoreFailure(KoansResult result);
	abstract protected void displayAllSuccess(KoansResult result);
}
package com.sandwich.koan.ui;

import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.util.io.ui.ErrorPresenter;

public interface SuitePresenter extends ErrorPresenter {

	public void displayResult(KoanSuiteResult result);
	public void displayError(String error);
	public void displayMessage(String error);
	public void clearMessages();

}

package com.sandwich.util.io.ui;

public class DefaultErrorPresenter implements ErrorPresenter {

	@Override
	public void displayError(String error) {
		System.err.println(error);
	}
	
}

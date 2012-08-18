package com.sandwich.util.io;

import java.io.File;

import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;

public class CompilationFailureLogger implements CompilationListener {
	public void compilationFailed(File src, String[] command, int exitCode, String errorMessage, Throwable x) {
		SuitePresenter presenter = ApplicationUtils.getPresenter();
		String lineSeparator = KoanConstants.EOL;
		presenter.displayError(lineSeparator+
				"*****************************************************************");
		presenter.displayError(lineSeparator+"Compile Output:");
		presenter.displayError(errorMessage.
				replace(lineSeparator, lineSeparator + "    ") + lineSeparator); // indent compiler output
		presenter.displayError("Compiling \""+src.getAbsolutePath() +"\" failed."); 
		presenter.displayError("The exit status was: " + exitCode);
		presenter.displayError(
				"*****************************************************************"+lineSeparator);
	}
	public void compilationSucceeded(File src, String[] command, String stdIo, Throwable x) { }
}
package com.sandwich.util.io;

import java.io.File;
import java.util.Arrays;

import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;

public class CompilationFailureLogger implements CompilationListener {
	public void compilationFailed(File src, String[] command, Process p, Throwable x) {			
		SuitePresenter presenter = ApplicationUtils.getPresenter();
		presenter.displayError("\n*****************************************************************");
		presenter.displayError(Arrays.toString(command));
		presenter.displayError(src.getAbsolutePath() + " does not compile. exit status was: " + p.exitValue());
		presenter.displayError("*****************************************************************\n");
	}
	public void compilationSucceeded(File src, String[] command, Process p, Throwable x) { }
}

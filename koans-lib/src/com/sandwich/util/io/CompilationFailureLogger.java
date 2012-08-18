package com.sandwich.util.io;

import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class CompilationFailureLogger implements CompilationListener {
	public void compilationFailed(File src, String[] command, Process p, Throwable x) {
		SuitePresenter presenter = ApplicationUtils.getPresenter();
		presenter.displayError("\n*****************************************************************");
		presenter.displayError("COMPILE FAILED! Exit status was: " + p.exitValue());
		presenter.displayError("\nCompile Output:");
		presenter.displayError(StreamUtils.convertStreamToString(p.getErrorStream()));
		presenter.displayError("Compile Command:");
		presenter.displayError(Arrays.toString(command));
		presenter.displayError("*****************************************************************\n");
		System.exit(1);
	}
	public void compilationSucceeded(File src, String[] command, Process p, Throwable x) { }
}

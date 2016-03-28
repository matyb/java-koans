package com.sandwich.util.io.filecompiler;

import java.io.File;

import com.sandwich.util.io.ui.DefaultErrorPresenter;
import com.sandwich.util.io.ui.ErrorPresenter;

public class CompilationFailureLogger implements CompilationListener {
	
	private ErrorPresenter presenter;
	
	public CompilationFailureLogger(){
		this(new DefaultErrorPresenter());
	}
	
	public CompilationFailureLogger(ErrorPresenter presenter){
		this.presenter = presenter;
	}
	
	public void compilationFailed(File src, String[] command, int exitCode, String errorMessage, Throwable x) {
		String lineSeparator = System.getProperty("line.separator");
		presenter.displayError(
				lineSeparator +
				"*****************************************************************" + lineSeparator +
				"Compile Output:" + lineSeparator +
				errorMessage.replace(lineSeparator, lineSeparator + "    ") + lineSeparator +
				"Compiling \"" + src.getAbsolutePath() + "\" failed." + lineSeparator +
				"The exit status was: " + exitCode + lineSeparator +
				"*****************************************************************" + lineSeparator +
				lineSeparator);
	}
	public void compilationSucceeded(File src, String[] command, String stdIo, Throwable x) { }
}
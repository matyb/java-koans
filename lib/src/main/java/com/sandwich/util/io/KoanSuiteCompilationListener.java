package com.sandwich.util.io;

import java.io.File;

import com.sandwich.util.io.filecompiler.CompilationListener;
import com.sandwich.util.io.filecompiler.FileCompilerAction;

public class KoanSuiteCompilationListener implements CompilationListener {

	private boolean lastCompilationAttemptFailed = false;
	private String lastMessageShown = null;
	
	public void compilationFailed(File src, String[] command, int exitCode, String errorMessage, Throwable x) {
		if(lastMessageShown == null || !errorMessage.trim().equals(lastMessageShown.trim())){
			FileCompilerAction.LOGGING_HANDLER.compilationFailed(src, command, exitCode, errorMessage, x);
		}
		lastMessageShown = errorMessage;
		lastCompilationAttemptFailed = true;
	}
	
	public void compilationSucceeded(File src, String[] command, String stdIo, Throwable x) {
		lastMessageShown = null; // reset last failed compilation message
		lastCompilationAttemptFailed = false;
	}
	
	public boolean isLastCompilationAttemptFailure(){
		return lastCompilationAttemptFailed;
	}
}

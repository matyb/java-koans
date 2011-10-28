package com.sandwich.util.io;

import java.io.File;

public class KoanSuiteCompilationListener implements CompilationListener {

	private boolean lastCompilationAttemptFailed = false;
	private boolean messageShown = false;
	
	public void compilationFailed(File src, String[] command, Process p, Throwable x) {
		if(!messageShown){
			FileCompilerAction.LOGGING_HANDLER.compilationFailed(src, command, p, x);
		}
		messageShown = lastCompilationAttemptFailed = true;
	}
	
	public void compilationSucceeded(File src, String[] command, Process p, Throwable x) {
		messageShown = lastCompilationAttemptFailed = false;
	}
	
	public boolean isLastCompilationAttemptFailure(){
		return lastCompilationAttemptFailed;
	}
}

package com.sandwich.util.io;

import java.io.File;
import java.util.Arrays;

public class CompilationFailureLogger implements CompilationListener {
	public void compilationFailed(File src, String[] command, Process p, Throwable x) {			
		System.out.println("\n*****************************************************************");
		System.out.println(Arrays.toString(command));
		System.out.println(src.getAbsolutePath() + " does not compile. exit status was: " + p.exitValue());
		System.out.println("*****************************************************************\n");
	}
	public void compilationSucceeded(File src, String[] command, Process p, Throwable x) { }
}

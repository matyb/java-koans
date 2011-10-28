package com.sandwich.util.io;

import java.io.File;

public interface CompilationListener {
	void compilationFailed(File src, String[] command, Process p, Throwable x);
	void compilationSucceeded(File src, String[] command, Process p, Throwable x);
}

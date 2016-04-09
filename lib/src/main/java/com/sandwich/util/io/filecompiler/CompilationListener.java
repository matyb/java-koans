package com.sandwich.util.io.filecompiler;

import java.io.File;

public interface CompilationListener {
	void compilationFailed(File src, String[] command, int exitCode, String errorMessage, Throwable x);
	void compilationSucceeded(File src, String[] command, String stdIo, Throwable x);
}

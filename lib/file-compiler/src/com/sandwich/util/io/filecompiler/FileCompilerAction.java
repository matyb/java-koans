package com.sandwich.util.io.filecompiler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sandwich.util.ExceptionUtils;
import com.sandwich.util.io.ForEachFileAction;
import com.sandwich.util.io.StreamUtils;

public class FileCompilerAction extends ForEachFileAction {
	
	private final String destinationPath;
	private final String[] classPaths;
	private CompilationListener compilationListener;
	private final long timeout;
	public static final CompilationListener LOGGING_HANDLER = new CompilationFailureLogger();
	
	public FileCompilerAction(File destination, CompilationListener errorHandler, String...classPaths){
		this(destination, errorHandler, 1000, classPaths);
	}
	
	public FileCompilerAction(File destination,
			CompilationListener errorHandler, long timeout, String[] classPaths) {
		if(destination == null){
			throw new IllegalArgumentException("the destination path is required");
		}
		this.destinationPath = destination.getAbsolutePath();
		this.classPaths = classPaths;
		this.compilationListener = errorHandler == null ? LOGGING_HANDLER : errorHandler;
		this.timeout = timeout;
	}
	
	public void onFile(File src) throws IOException {
		String fileName = src.getName();
		if (CompilerConfig.isSourceFile(fileName)) {
			String[] command = CompilerConfig.getCompilationCommand(src, destinationPath, getClasspath());
			try{
				Process p = Runtime.getRuntime().exec(command);
				try {
					executeWithTimeout(src, command, p, timeout);
				} catch (IllegalThreadStateException x) {
					compilationListener.compilationFailed(src, command, 
							255, "Compilation took longer than "+timeout+" ms.\n" +
									"It is likely that the compiler has locked up compiling this file.\n" +
									"Please revert your last change and try something different.", x);
				} catch (Exception x) {
					x.printStackTrace();
					compilationListener.compilationFailed(src, command, 
							p.exitValue(), StreamUtils.convertStreamToString(p.getErrorStream()), x);
				}
			}catch(IOException x){
				if(x.getMessage().contains("Cannot run program")){
					String commandString = "";
					for(String segment : command){
						commandString += segment + " ";
					}
					commandString = commandString.trim();
					compilationListener.compilationFailed(src, command, 2, 
							"Cannot execute:" + System.getProperty("line.separator") +  
							commandString + System.getProperty("line.separator") +
							"Please check that the appropriate compiler (" + command[0] + ") is installed, is executable and is listed in your PATH environment variable value.", x);
				}
			}
		}
	}

	private void executeWithTimeout(final File src, final String[] command, 
			final Process p, final long timeout) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
		   public Object call() {
				try {
					if (p.waitFor() != 0) {
						compilationListener.compilationFailed(src, command, p
								.exitValue(), StreamUtils.convertStreamToString(p.getErrorStream()), null);
					} else {
						compilationListener.compilationSucceeded(src, command,
								StreamUtils.convertStreamToString(p.getInputStream()), null);
					}
				} catch (InterruptedException e) {
					compilationListener.compilationFailed(src, command, p.exitValue(), 
							ExceptionUtils.convertToPopulatedStackTraceString(e), null);
				}
				return null;
		   }
		};
		Future<Object> future = executor.submit(task);
		try {
			future.get(timeout, TimeUnit.MILLISECONDS); 
		} catch (Exception e) {
			compilationListener.compilationFailed(src, command, p.exitValue(), 
					ExceptionUtils.convertToPopulatedStackTraceString(e), null);
		}
	}
	
	private String getClasspath() {
		String classPath = "";
		for(String jar : classPaths) {
		    classPath += jar + File.pathSeparatorChar;
		}
		return classPath;
	}
	
}

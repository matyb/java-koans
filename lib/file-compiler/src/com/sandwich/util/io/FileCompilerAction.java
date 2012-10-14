package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sandwich.util.ExceptionUtils;

class FileCompilerAction implements FileAction {
	
	private final String destinationPath;
	private final String[] classPaths;
	private CompilationListener compilationListener;
	private final long timeout;
	static final CompilationListener LOGGING_HANDLER = new CompilationFailureLogger();
	
	public FileCompilerAction(File destinationPath, CompilationListener errorHandler, String...classPaths){
		this(destinationPath, errorHandler, 1000, classPaths);
	}
	
	public FileCompilerAction(File destinationPath,
			CompilationListener errorHandler, long timeout, String[] classPaths) {
		if(destinationPath == null){
			throw new IllegalArgumentException("the destination path is required");
		}
		this.destinationPath = destinationPath.getAbsolutePath();
		this.classPaths = classPaths;
		this.compilationListener = errorHandler == null ? LOGGING_HANDLER : errorHandler;
		this.timeout = timeout;
	}
	
	public void sourceToDestination(File src, File bin) throws IOException {
		String fileName = src.getName();
		if (fileName.length() > 4 && fileName.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)) {
			String[] command = constructJavaCompilationCommand(src);
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
	
	private String[] constructJavaCompilationCommand(File src) {
		return copy(new String[]{"javac", "-d", destinationPath}, getClasspath(), new String[]{src.getAbsolutePath()});
	}

	private String[] copy(String[]...strings) {
		String[] copies = new String[getTotalSize(strings)];
		int i = 0;
		for(String[] strings2 : strings){
			for(String string : strings2){
				copies[i++] = string;
			}
		}
		return copies;
	}

	private int getTotalSize(String[][] strings) {
		int i = 0;
		for(String[] strings2 : strings){
			i += strings2.length;
		}
		return i;
	}

	private String[] getClasspath() {
		String[] classpaths = new String[classPaths.length + 1];
		if (classPaths.length > 0) {
			classpaths[0]  = "-classpath";
		}else{
			return new String[]{};
		}
		for(int i = 1; i <= classPaths.length; i++){
			classpaths[i] = classPaths[i - 1];
		}
		return classpaths;
	}

	public File makeDestination(File dest, String fileInDirectory) {
		String fileName = dest.getName();
		fileName = fileName.length() > 4
				&& fileName.toLowerCase().contains(FileCompiler.JAVA_SUFFIX) ? fileName
				.replace(FileCompiler.JAVA_SUFFIX, FileCompiler.CLASS_SUFFIX) : fileName;
		dest = new File(dest, fileInDirectory);
		System.out.println("file: " + dest.getAbsolutePath());
		return dest;
	}
	
}

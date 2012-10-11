package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

class FileCompilerAction implements FileAction {
	
	private final String destinationPath;
	private final String[] classPaths;
	private CompilationListener compilationListner;
	static final CompilationListener LOGGING_HANDLER = new CompilationFailureLogger();
	
	public FileCompilerAction(File destinationPath, CompilationListener errorHandler, String...classPaths){
		if(destinationPath == null){
			throw new IllegalArgumentException("the destination path is required");
		}
		this.destinationPath = destinationPath.getAbsolutePath();
		this.classPaths = classPaths;
		this.compilationListner = errorHandler == null ? LOGGING_HANDLER : errorHandler;
	}
	
	public void sourceToDestination(File src, File bin) throws IOException {
		String fileName = src.getName();
		if (fileName.length() > 4 && fileName.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)) {
			String[] command = constructJavaCompilationCommand(src);
			Process p = Runtime.getRuntime().exec(command);
			try {
				if (p.waitFor() != 0) {
					compilationListner.compilationFailed(src, command, p.exitValue(), StreamUtils.convertStreamToString(p.getErrorStream()), null);
				}else{
					compilationListner.compilationSucceeded(src, command, StreamUtils.convertStreamToString(p.getInputStream()), null);
				}
			} catch (Exception x) {
				x.printStackTrace();
				compilationListner.compilationFailed(src, command, 
						p.exitValue(), StreamUtils.convertStreamToString(p.getErrorStream()), x);
			}
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

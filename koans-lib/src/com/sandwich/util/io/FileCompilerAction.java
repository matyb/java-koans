package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;

class FileCompilerAction implements FileAction {
	
	private final String destinationPath;
	private final String[] classPaths;
	private CompilationListener errorHandler;
	static final CompilationListener LOGGING_HANDLER = new CompilationFailureLogger();
	
	public FileCompilerAction(File destinationPath, CompilationListener errorHandler, String...classPaths){
		if(destinationPath == null){
			throw new IllegalArgumentException("the destination path is required");
		}
		this.destinationPath = destinationPath.getAbsolutePath();
		this.classPaths = classPaths;
		this.errorHandler = errorHandler == null ? LOGGING_HANDLER : errorHandler;
	}
	
	public void sourceToDestination(File src, File bin) throws IOException {
		// javac -d ..\bin -classpath ..\lib\koans.jar beginner\*.java
		String fileName = src.getName();
		if (fileName.length() > 4
				&& fileName.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)) {
			String[] command = constructJavaCompilationCommand(src);
			SuitePresenter presenter = ApplicationUtils.getPresenter();
			if (ApplicationSettings.isDebug()) {
				presenter.displayMessage("executing command: \"" + command
						+ "\" to compile the sourcefile: " + src + ".");
			}
			Process p = Runtime.getRuntime().exec(command);
			try {
				if (ApplicationSettings.isDebug()) {
					presenter.displayMessage("compiling file: " + src.getAbsolutePath());
				}
				if (p.waitFor() != 0) {
					errorHandler.compilationFailed(src, command, p, null);
				}else{
					errorHandler.compilationSucceeded(src, command, p, null);
				}
			} catch (Exception x) {
				x.printStackTrace();
				errorHandler.compilationFailed(src, command, p, x);
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

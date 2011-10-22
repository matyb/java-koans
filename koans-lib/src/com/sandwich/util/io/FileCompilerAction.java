package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.sandwich.koan.constant.ApplicationSettings;

class FileCompilerAction implements FileAction {
	
	private final String destinationPath;
	private final String[] classPaths;
	
	public FileCompilerAction(File destinationPath, String...classPaths){
		this.destinationPath = destinationPath.getAbsolutePath();
		this.classPaths = classPaths;
	}
	
	public void sourceToDestination(File src, File bin) throws IOException {
		// javac -d ..\bin -classpath ..\lib\koans.jar beginner\*.java
		String fileName = src.getName();
		if (fileName.length() > 4
				&& fileName.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)) {
			String[] command = constructJavaCompilationCommand(src);
			if (ApplicationSettings.isDebug()) {
				System.out.println("executing command: \"" + command
						+ "\" to compile the sourcefile: " + src + ".");
			}
			Process p = Runtime.getRuntime().exec(command);
			try {
				if (ApplicationSettings.isDebug()) {
					System.out.println("compiling file: " + src.getAbsolutePath());
				}
				if (p.waitFor() != 0) {
					compilationFailed(src, command, p, null);
				}
			} catch (Exception x) {
				x.printStackTrace();
				compilationFailed(src, command, p, x);
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

	private void compilationFailed(File src, String[] command, Process p, Throwable x) {
		System.out.println("\n*****************************************************************");
		System.out.println(Arrays.toString(command));
		System.out.println(src.getAbsolutePath() + " does not compile. exit status was: " + p.exitValue());
		System.out.println("*****************************************************************\n");
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

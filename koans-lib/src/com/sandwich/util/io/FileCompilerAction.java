package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

import com.sandwich.koan.constant.KoanConstants;

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
			String command = constructJavaCompilationCommand(src);
			if (KoanConstants.DEBUG) {
				System.out.println("executing command: \"" + command
						+ "\" to compile the sourcefile: " + src + ".");
			}
			if(System.getProperty("os.name").toLowerCase().contains("mac")){
				command = command.replace("\"", "");
			}
			Process p = Runtime.getRuntime().exec(command);
			try {
				if (KoanConstants.DEBUG) {
					System.out.println("compiling file: " + src.getAbsolutePath());
				}
				if (p.waitFor() != 0) {
					compilationFailed(src, command, p);
				}
			} catch (Exception x) {
				x.printStackTrace();
				compilationFailed(src, command, p);
			}
		}
	}

	private String constructJavaCompilationCommand(File src) {
		String command = "javac -d \""
				+ destinationPath + "\" "
				+ getClasspath()+"\"" + src.getAbsolutePath()+"\"";
		return command;
	}

	private void compilationFailed(File src, String command, Process p) {
		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("  CHECK THAT THE DIR THIS IS RUN IN IS NOT IN A DIR WITH SPACES");
		System.out.println("*****************************************************************");
		System.out.println(command);
		System.out.println(src.getAbsolutePath()
				+ " does not compile. exit status was: "
				+ p.exitValue());
		System.out
				.println("*****************************************************************");
		System.out.println();
	}

	private String getClasspath() {
		StringBuilder builder = new StringBuilder();
		if (classPaths.length > 0) {
			builder.append(" -classpath ");
		}
		for (String classpath : classPaths) {
			builder.append("\"").append(classpath).append("\" ");
		}
		return builder.toString();
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

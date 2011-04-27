package com.sandwich.util;

import static com.sandwich.koan.constant.KoanConstants.FILESYSTEM_SEPARATOR;

import java.io.File;
import java.io.IOException;

public class FileCompiler {
	
	private static final String JAVA_SUFFIX = ".java";
	private static final String CLASS_SUFFIX = 	".class";

	public static void compileRelative(String parentFolder, String src, String bin) throws IOException{
		compileAbsolute(FileUtils.makeAbsolute(new StringBuilder(parentFolder).append(FILESYSTEM_SEPARATOR).append(src).toString()),
				FileUtils.makeAbsolute(new StringBuilder(parentFolder).append(FILESYSTEM_SEPARATOR).append(bin).toString()));
	}

	public static void compileAbsolute(String src, String bin) throws IOException {
		compile(new File(src), new File(bin));
	}

	public static void compile(File src, File bin) throws IOException {
		final File destinationDirectory = bin;
		FileUtils.forEachFile(src, bin, new FileAction(){
			public void sourceToDestination(File src, File bin) throws IOException {
				//javac -d ..\bin -classpath ..\lib\koans.jar beginner\*.java
				String fileName = src.getName();
				if(fileName.length() > 4 && fileName.toLowerCase().endsWith(JAVA_SUFFIX)){
					String command = "javac -d "+destinationDirectory.getAbsolutePath()+
						" -classpath .."+FILESYSTEM_SEPARATOR+"lib"+FILESYSTEM_SEPARATOR+"* "+src.getAbsolutePath();
//					Process p = Runtime.getRuntime().exec(command);
					System.out.println("command: "+command);
				}
			}
			public File makeDestination(File dest, String fileInDirectory) {
				String fileName = dest.getName();
				fileName = fileName.length() > 4 && fileName.toLowerCase().contains(JAVA_SUFFIX) ? 
						fileName.replace(JAVA_SUFFIX, CLASS_SUFFIX) : fileName;
				dest = new File(dest, fileInDirectory);
				System.out.println("file: "+dest.getAbsolutePath());
				return dest;
			}
		});
	}
	
}

package com.sandwich.util.io.filecompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class CompilerConfig {

	private static final ResourceBundle commandBySuffixRB = ResourceBundle.getBundle("com.sandwich.util.io.filecompiler.compilationcommands");
	
	public static boolean isSourceFile(String fileName) {
		return commandBySuffixRB.containsKey(getSuffix(fileName));
	}
	
	public static String[] getCompilationCommand(File src, String destinationPath, String classPath) {
		String absolutePath = src.getAbsolutePath();
		String command = commandBySuffixRB.getString(getSuffix(absolutePath));
		if(command == null){
			throw new RuntimeException("Do not know how to compile " + absolutePath);
		}
		List<String> splitCommand = Arrays.asList(command.split(" "));
		List<String> commandSegments = new ArrayList<String>();
		for(String segment : splitCommand){
			String lowerCaseSegment = segment.toLowerCase();
			if("${bindir}".equals(lowerCaseSegment)){
				commandSegments.add(destinationPath);
			}else if("${classpath}".equals(lowerCaseSegment)){
				commandSegments.add(classPath);
			}else if("${filename}".equals(lowerCaseSegment)){
				commandSegments.add(src.getAbsolutePath());
			}else{
				commandSegments.add(segment);
			}
		}
		return commandSegments.toArray(new String[commandSegments.size()]);
	}

	public static Collection<String> getSupportedFileSuffixes() {
		return commandBySuffixRB.keySet();
	}
	
	public static String getSuffix(String fileName) {
		if(fileName != null){
			int periodIndex = fileName.indexOf('.');
			if(periodIndex > -1){
				return fileName.substring(periodIndex).toLowerCase();
			}
		}
		return "";
	}

	public static boolean isSuffixSupported(String suffix) {
		return getSupportedFileSuffixes().contains(suffix);
	}
	
}

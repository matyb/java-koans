package com.sandwich.util;

import java.util.Comparator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sandwich.koan.KoanMethod;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.filecompiler.FileCompiler;

public class KoanComparator implements Comparator<KoanMethod> {
	
	public int compare(KoanMethod arg0, KoanMethod arg1) {
		Class<?> declaringClass0 = arg0.getMethod().getDeclaringClass();
		Class<?> declaringClass1 = arg1.getMethod().getDeclaringClass();
		if(declaringClass0 != declaringClass1){
			Logger.getAnonymousLogger().severe("no idea how to handle comparing the classes: " + declaringClass0 + " and: "+declaringClass1);
			return 0;
		}
		String contentsOfOriginalJavaFile = FileCompiler.getContentsOfJavaFile(DirectoryManager.getSourceDir(), declaringClass0.getName());
		String pattern = ".*\\s%s(\\(|\\s*\\))";
		Integer index0 = indexOfMatch(contentsOfOriginalJavaFile, String.format(pattern, arg0.getMethod().getName()));
		Integer index1 = indexOfMatch(contentsOfOriginalJavaFile, String.format(pattern, arg1.getMethod().getName()));
		return index0.compareTo(index1);
	}
	
	/*
	 * TODO: This is utility code...
	 */
	private int indexOfMatch(String inputString, String pattern) {
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(inputString);
	    if (m.find()) {
	       return m.start();
	    } 
	    return -1;
	}

}
package com.sandwich.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.sandwich.koan.KoanMethod;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.filecompiler.FileCompiler;

public class KoanComparator implements Comparator<KoanMethod> {
	List<String> orderedKeywords;
	Set<String> methodNamesCompared = new HashSet<String>();
	
	public KoanComparator(String...koans){
		this(Arrays.asList(koans));
	}
	
	public KoanComparator(Collection<String> koans){
		this.orderedKeywords = new ArrayList<String>(koans);
	}
	
	public int compare(KoanMethod arg0, KoanMethod arg1) {
		Class<?> declaringClass0 = arg0.getMethod().getDeclaringClass();
		Class<?> declaringClass1 = arg1.getMethod().getDeclaringClass();
		if(declaringClass0 != declaringClass1){
			Logger.getAnonymousLogger().severe("no idea how to handle comparing the classes: " + declaringClass0 + " and: "+declaringClass1);
			return 0;
		}
		String contentsOfOriginalJavaFile = FileCompiler.getContentsOfJavaFile(DirectoryManager.getSourceDir(), declaringClass0.getName());
		Integer index0 = Integer.valueOf(	contentsOfOriginalJavaFile.indexOf(arg0.getMethod().getName()));
		Integer index1 = Integer.valueOf(	contentsOfOriginalJavaFile.indexOf(arg1.getMethod().getName()));
		return index0.compareTo(index1);
	}

}
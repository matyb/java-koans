package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class FileOperation implements FileAction {

	private List<String> ignoredPaths;
	
	public FileOperation(String... ignoredPaths){
		this(Arrays.asList(ignoredPaths));
	}
	
	public FileOperation(List<String> ignoredPaths){
		this.ignoredPaths = ignoredPaths;
	}
	
	public void operate(File file) throws IOException {
		if(!isIgnored(file)){
			if(file == null){
				onNull(file);
			}else if(!file.exists()){
				onNew(file);
			}else if(file.isDirectory()){
				onDirectory(file);
			}else{
				onFile(file);
			}
		}
	}
	
	boolean isIgnored(File file){
		boolean ignore = false;
		while(file != null){
			String end = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(System.getProperty("file.separator")) + 1);
			for(String pathToIgnore: ignoredPaths){
				ignore = end.matches(pathToIgnore);
				if(ignore){
					return true;
				}
			}
			file = file.getParentFile();
		}
		return ignore;
	}

}

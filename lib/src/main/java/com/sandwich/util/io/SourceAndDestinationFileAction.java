package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

public abstract class SourceAndDestinationFileAction extends ForEachFileAction {

	private final File source;
	private final File destination;
	
	public SourceAndDestinationFileAction(String sourceDir, String destinationDir){
		this(new File(sourceDir), new File(destinationDir));
	}
	
	public SourceAndDestinationFileAction(File source, File destination){
		this.source = assertIsDirectory(source);
		this.destination = assertIsDirectory(destination);
	}

	public void operate() throws IOException {
		super.operate(source);
	}
	
	public void onFile(File file) throws IOException {
		String subDir = file.getAbsolutePath().replace(source.getAbsolutePath(), "");
		File dest = new File(destination.getAbsolutePath() + File.separator + subDir);
		sourceToDestination(file, dest);
	}
	
	abstract public void sourceToDestination(File src, File dest) throws IOException;
	
	private File assertIsDirectory(File file) {
		if(file == null || !file.isDirectory()){
			throw new IllegalArgumentException(file + " is not a directory.");
		}
		return file;
	}
	
}

package com.sandwich.koan;

import java.io.File;

import com.sandwich.util.io.DynamicClassLoader;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.directories.DirectoryManager;

public class KoanClassLoader extends DynamicClassLoader {

	private static DynamicClassLoader instance;
	private FileMonitor fileMonitor;
	
	private KoanClassLoader(){
		super(DirectoryManager.getBinDir(), 
			  DirectoryManager.getSourceDir(), 
			  buildClassPath(), 
			  DynamicClassLoader.class.getClassLoader(), 
			  ApplicationSettings.getFileCompilationTimeoutInMs());
		this.fileMonitor = FileMonitorFactory.getInstance(new File(DirectoryManager.getMainDir()), new File(DirectoryManager.getDataFile()));
	}

	private static String[] buildClassPath() {
		File[] jars = new File(DirectoryManager.getProjectLibraryDir()).listFiles();
		String[] classPath = new String[jars.length];
		for (int i = 0; i < jars.length; i++) {
			if(jars[i].getAbsolutePath().toLowerCase().endsWith(".jar")){
				classPath[i] = jars[i].getAbsolutePath();
			}
		}
		return classPath;
	}
	
	public static void setInstance(DynamicClassLoader loader){
		instance = loader;
	}
	
	synchronized public static DynamicClassLoader getInstance(){
		if(instance == null){
			instance = new KoanClassLoader();
		}
		return instance;
	}
	
	@Override
	public boolean isFileModifiedSinceLastPoll(String sourcePath, long lastModified) {
		return fileMonitor.isFileModifiedSinceLastPoll(sourcePath, lastModified);
	}
	
	@Override
	public void updateFileSavedTime(File sourceFile) {
		fileMonitor.updateFileSaveTime(sourceFile);
	}
	
}

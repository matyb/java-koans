package com.sandwich.koan;

import java.io.File;
import java.io.UTFDataFormatException;

import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.classloader.DynamicClassLoader;
import com.sandwich.util.io.directories.DirectoryManager;

public class KoanClassLoader extends DynamicClassLoader {

	private static KoanClassLoader instance;
	private final FileMonitor fileMonitor;
	
	private KoanClassLoader(){
		this(DirectoryManager.getBinDir(),
				DirectoryManager.getSourceDir(), 
				buildClassPath(),
				DynamicClassLoader.class.getClassLoader(),
				ApplicationSettings.getFileCompilationTimeoutInMs(),
				getFileMonitor());
	}

	private static FileMonitor getFileMonitor() {
		try{
			return FileMonitorFactory.getInstance(new File(DirectoryManager.getMainDir()), new File(DirectoryManager.getDataFile()));
		}catch(RuntimeException x){
			if(x.getCause() instanceof UTFDataFormatException){
				ApplicationUtils.getPresenter().displayError("Issue loading file system hashes, please rerun run.bat or run.sh with the -clear switch to reset.");
				System.exit(4);
			}
			throw x;
		}
	}

	private KoanClassLoader(String binDir, String sourceDir,
			String[] classPath, ClassLoader parent,
			long timeout, FileMonitor fileMonitor) {
		super(binDir, sourceDir, classPath, parent, timeout);
		this.fileMonitor = fileMonitor;
	}
	
	@Override
	public boolean isFileModifiedSinceLastPoll(String sourcePath, long lastModified) {
		return fileMonitor.isFileModifiedSinceLastPoll(sourcePath, lastModified);
	}

	@Override
	public void updateFileSavedTime(File sourceFile) {
		fileMonitor.updateFileSaveTime(sourceFile);
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
	
	synchronized public static DynamicClassLoader getInstance(){
		if(instance == null){
			instance = new KoanClassLoader();
		}
		return instance.clone();
	}
	
	@Override
	public KoanClassLoader clone(){
		return new KoanClassLoader(getBinDir(), getSourceDir(), getClassPath(), getClass().getClassLoader(), getTimeout(), fileMonitor);
	}
	
}

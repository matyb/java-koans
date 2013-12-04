package com.sandwich.koan;

import java.io.File;

import com.sandwich.util.io.DynamicClassLoader;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.directories.DirectoryManager;

public class KoanClassLoader extends ClassLoader {

	private static DynamicClassLoader instance;

	public static DynamicClassLoader createInstance() {
		String binDir = DirectoryManager.getBinDir();
		String sourceDir = DirectoryManager.getSourceDir();
		File[] jars = new File(DirectoryManager.getProjectLibraryDir()).listFiles();
		String[] classPath = new String[jars.length];
		for (int i = 0; i < jars.length; i++) {
		    classPath[i] = jars[i].getAbsolutePath();
		}
		FileMonitor fileMonitor = FileMonitorFactory.getInstance(
				new File(DirectoryManager.getMainDir()), new File(DirectoryManager.getDataFile()));
		ClassLoader classLoader = DynamicClassLoader.class.getClassLoader();
		long timeout = ApplicationSettings.getFileCompilationTimeoutInMs();
		return new DynamicClassLoader(binDir, sourceDir, classPath, fileMonitor, classLoader, timeout);
	}
	
	public static void setInstance(DynamicClassLoader loader){
		instance = loader;
	}
	
	synchronized public static DynamicClassLoader getInstance(){
		if(instance == null){
			instance = createInstance();
		}
		return instance.clone();
	}
	
}

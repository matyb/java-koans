package com.sandwich.koan;

import java.io.File;

import com.sandwich.util.io.DynamicClassLoader;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.directories.DirectoryManager;

public class KoanClassLoader extends ClassLoader {

	private static DynamicClassLoader instance = createInstance();

	public static DynamicClassLoader createInstance() {
		String binDir = DirectoryManager.getBinDir();
		String sourceDir = DirectoryManager.getSourceDir();
		String[] classPath = new String[]{DirectoryManager.getProjectLibraryDir() + 
				DirectoryManager.FILESYSTEM_SEPARATOR + "koans.jar"};
		FileMonitor fileMonitor = FileMonitorFactory.getInstance(
				new File(DirectoryManager.getProdMainDir()), new File(DirectoryManager.getDataFile()));
		ClassLoader classLoader = DynamicClassLoader.class.getClassLoader();
		long timeout = ApplicationSettings.getFileCompilationTimeoutInMs();
		return new DynamicClassLoader(binDir, sourceDir, classPath, fileMonitor, classLoader, timeout);
	}
	
	public static void setInstance(DynamicClassLoader loader){
		instance = loader;
	}
	
	public static DynamicClassLoader getInstance(){
		return instance.clone();
	}
	
}

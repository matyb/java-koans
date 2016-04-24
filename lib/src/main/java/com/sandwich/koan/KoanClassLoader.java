package com.sandwich.koan;

import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.classloader.DynamicClassLoader;
import com.sandwich.util.io.directories.DirectoryManager;

import java.io.File;
import java.io.UTFDataFormatException;
import java.net.URL;
import java.net.URLClassLoader;

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

	public static String[] buildClassPath() {
    String projectLibraryDir = DirectoryManager.getProjectLibraryDir();
    File[] jars = new File(projectLibraryDir).listFiles();
    if (jars == null || jars.length == 0) {
      // Tolerate the "app/lib" directory being empty and put the jar files
      // for this class's class loader onto the path
      jars = getFilesOnTheClassLoaderClassPath();
    }
		String[] classPath = new String[jars.length];
		for (int i = 0; i < jars.length; i++) {
			String jarPath = jars[i].getAbsolutePath();
			String jarPathLowerCase = jarPath.toLowerCase();
			if (jarPathLowerCase.endsWith(".jar") || jarPathLowerCase.endsWith(".war")) {
				classPath[i] = jarPath;
			}
		}
		return classPath;
	}

  private static File[] getFilesOnTheClassLoaderClassPath() {
    ClassLoader loader = KoanClassLoader.class.getClassLoader();
    if (loader instanceof URLClassLoader) {
      URLClassLoader urlClassLoader = (URLClassLoader) loader;
      URL[] urls = urlClassLoader.getURLs();
      File[] files = new File[urls.length];
      for (int i = 0; i < urls.length; i++) {
        files[i] = urlToFile(urls[i]);
      }
      return files;

    } else {
      return new File[0];
    }
  }

  private static File urlToFile(URL url) {
    return new File(url.getFile());
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

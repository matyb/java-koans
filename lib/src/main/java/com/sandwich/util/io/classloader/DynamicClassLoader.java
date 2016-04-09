package com.sandwich.util.io.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.util.io.filecompiler.CompilationListener;
import com.sandwich.util.io.filecompiler.CompilerConfig;
import com.sandwich.util.io.filecompiler.FileCompiler;
import com.sandwich.util.io.filecompiler.FileCompilerAction;

public abstract class DynamicClassLoader extends ClassLoader {

	private static Map<URL, Class<?>> classesByLocation = new HashMap<URL, Class<?>>();
	private static Map<Class<?>, URL> locationByClass = new HashMap<Class<?>, URL>();
	
	private final long timeout;
	private final String binDir, sourceDir;
	private final String[] classPath;
	
	public DynamicClassLoader(String binDir, String sourceDir, String[] classPath){
		this(binDir, sourceDir, classPath, ClassLoader.getSystemClassLoader());
	}
	
	public DynamicClassLoader(String binDir, String sourceDir, String[] classPath, ClassLoader parent) {
        	this(binDir, sourceDir, classPath, parent, 5000l);
    	}

	public DynamicClassLoader(String binDir, String sourceDir,
			String[] classPath, ClassLoader parent,
			long timeout) {
		super(parent);
        	this.binDir = binDir;
        	this.sourceDir = sourceDir;
        	this.classPath = classPath;
        	this.timeout = timeout;
	}

	public abstract boolean isFileModifiedSinceLastPoll(String sourcePath, long lastModified);
	
	public abstract void updateFileSavedTime(File sourceFile);
	
	public static void remove(URL url){
		String urlToString = url.toString().replace(FileCompiler.CLASS_SUFFIX, "");
		for(String suffix : CompilerConfig.getSupportedFileSuffixes()){
			urlToString.replace(suffix, "");
		}
		for(Entry<URL, Class<?>> entry : classesByLocation.entrySet()){
			if(entry.getKey().toString().contains(urlToString)){
				locationByClass.remove(entry.getValue());
				entry.setValue(null);
			}
		}
	}
	
	public static void remove(Class<?> clas){
		for(Entry<Class<?>, URL> entry : locationByClass.entrySet()){
			if(entry.getKey().getName().contains(clas.getName())){
				classesByLocation.remove(entry.getValue());
				entry.setValue(null);
			}
		}
	}
	
	public Class<?> loadClass(String className){
        	return loadClass(className, FileCompilerAction.LOGGING_HANDLER);
	} 
	
	public Class<?> loadClass(String className, CompilationListener listener){
		String fileSeparator = System.getProperty("file.separator");
		String fileName = binDir + fileSeparator
					+ className.replace(".", fileSeparator)
					+ FileCompiler.CLASS_SUFFIX;
		File classFile = new File(fileName);
		try {
			// file may have never been compiled, go ahead and compile it now
			File sourceFile = FileCompiler.classToSource(binDir, sourceDir, classFile);
			if(classFile.exists()){
				String absolutePath = classFile.getAbsolutePath();
				boolean isAnonymous = absolutePath.contains("$");
				if(isFileModifiedSinceLastPoll(sourceFile.getAbsolutePath(), sourceFile.lastModified())){
					if(!isAnonymous){
						compile(fileName, sourceFile, timeout, listener);
					}
				}
				return loadClass(classFile.toURI().toURL(), className);
			}
			try{
				return super.loadClass(className);
			}catch(ClassNotFoundException x){
				compile(fileName, sourceFile, timeout, listener);
				classFile = new File(fileName);
				return loadClass(classFile.toURI().toURL(), className);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void compile(String fileName, File sourceFile, long timeout, CompilationListener listener)
			throws IOException {
		FileCompiler.compile(sourceFile, new File(binDir), listener, timeout, classPath);
		updateFileSavedTime(sourceFile);
	}

	public Class<?> loadClass(URL url, String className){
		Class<?> clazz = classesByLocation.get(url);
		if(clazz != null){
			return clazz;
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			URLConnection connection = url.openConnection();
			InputStream input = connection.getInputStream();
	        int data = input.read();
	        while(data != -1){
	            buffer.write(data);
	            data = input.read();
	        }
	        input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        	byte[] classData = buffer.toByteArray();
        	clazz = defineClass(className, classData, 0, classData.length);
        	classesByLocation.put(url, clazz);
        	locationByClass.put(clazz, url);
        	return clazz;
	}

	public long getTimeout() {
		return timeout;
	}

	public String getBinDir() {
		return binDir;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public String[] getClassPath() {
		return classPath;
	}
	
}

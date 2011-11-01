package com.sandwich.util.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.util.io.directories.DirectoryManager;

public class DynamicClassLoader extends ClassLoader {

	private static Map<URL, Class<?>> classesByLocation = new HashMap<URL, Class<?>>();
	private static Map<Class<?>, URL> locationByClass = new HashMap<Class<?>, URL>();
	private final FileMonitor fileMonitor = FileMonitorFactory.getInstance(
											DirectoryManager.getProdMainDir());
	
	public DynamicClassLoader(){
		this(ClassLoader.getSystemClassLoader());
	}
	
	public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

	public static void remove(URL url){
		String urlToString = url.toString().replace(FileCompiler.CLASS_SUFFIX, "").replace(FileCompiler.JAVA_SUFFIX, "");
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
		String fileName = DirectoryManager.getBinDir()
						+ DirectoryManager.FILESYSTEM_SEPARATOR
						+ className.replace(KoanConstants.PERIOD, DirectoryManager.FILESYSTEM_SEPARATOR)
						+ FileCompiler.CLASS_SUFFIX;
		File classFile = new File(fileName);
		try {
			// file may have never been compiled, go ahead and compile it now
			File sourceFile = FileUtils.classToSource(classFile);
			if(classFile.exists()){
				String absolutePath = classFile.getAbsolutePath();
				boolean isAnonymous = absolutePath.contains("$");
				if(fileMonitor.isFileModifiedSinceLastPoll(sourceFile.getAbsolutePath(), sourceFile.lastModified())){
					if(!isAnonymous){
						compile(className, fileName, sourceFile, listener);
					}
				}
				return loadClass(classFile.toURI().toURL(), className);
			}
			try{
				return super.loadClass(className);
			}catch(ClassNotFoundException x){
				compile(className, fileName, sourceFile, listener);
				classFile = new File(fileName);
				return loadClass(classFile.toURI().toURL(), className);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void compile(String className, String fileName, File sourceFile, CompilationListener listener)
			throws IOException {
		FileCompiler.compile(sourceFile, 
				new File(DirectoryManager.getBinDir()),
				listener,
				new String[]{DirectoryManager.getProjectLibraryDir() + DirectoryManager.FILESYSTEM_SEPARATOR + "koans.jar"});
		fileMonitor.updateFileSaveTime(sourceFile);
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
}

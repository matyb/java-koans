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

public class DynamicClassLoader extends ClassLoader {

	private static Map<URL, Class<?>> classesByLocation = new HashMap<URL, Class<?>>();
	private static Map<Class<?>, URL> locationByClass = new HashMap<Class<?>, URL>();
	
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
		File classFile = new File(FileUtils.makeAbsoluteRelativeTo("koans")
				+ KoanConstants.FILESYSTEM_SEPARATOR
				+ KoanConstants.BIN_FOLDER
				+ KoanConstants.FILESYSTEM_SEPARATOR
				+ className.replace(KoanConstants.PERIOD, KoanConstants.FILESYSTEM_SEPARATOR)
				+ FileCompiler.CLASS_SUFFIX);
		try {
			if(classFile.exists()){
				return loadClass(classFile.toURI().toURL(), className);
			}
			return super.loadClass(className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

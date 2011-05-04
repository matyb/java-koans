package com.sandwich.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DynamicClassLoader extends ClassLoader {

	public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

	public Class<?> loadClass(URL url, String className){
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
        return defineClass(className, classData, 0, classData.length);
	}
}

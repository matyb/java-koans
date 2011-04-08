package com.sandwich.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	public static void copy(String fileName0, String fileName1) throws IOException{
		copy(new File(fileName0), new File(fileName1));
	}
	
	public static void copy(File file0, File file1) throws IOException{
		if((file0 == null || !file0.exists()) && file1 == null){
			throw new IllegalArgumentException("Both path's must actually exist");
		}
		if (file0.isDirectory()) {
			if (!file1.exists()) {
				file1.mkdir();
			}
			String files[] = file0.list();
			for (int i = 0; i < files.length; i++) {
				copy(new File(file0, files[i]), new File(file1, files[i]));
			}
		} else {
			if (!file0.exists()) {
				System.out.println("File or directory does not exist: "+file0);
				System.exit(0);
			} else {
				InputStream in = new FileInputStream(file0);
				OutputStream out = new FileOutputStream(file1);
				
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			}

		}
	}
	
}

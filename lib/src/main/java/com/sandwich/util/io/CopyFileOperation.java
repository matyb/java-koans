package com.sandwich.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFileOperation extends SourceAndDestinationFileAction {
	
	public CopyFileOperation(File source, File destination) {
		super(source, destination);
	}

	public CopyFileOperation(String sourceDir, String destinationDir) {
		super(sourceDir, destinationDir);
	}

	public void sourceToDestination(File src, File dest) throws IOException {
		InputStream in = new FileInputStream(src);
		if(!dest.getParentFile().exists()){
			dest.getParentFile().mkdirs();
		}
		OutputStream out = new FileOutputStream(dest);
		
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

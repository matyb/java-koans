package com.sandwich.koan.cmdline.behavior;

import static com.sandwich.koan.constant.KoanConstants.FILESYSTEM_SEPARATOR;

import java.io.File;
import java.io.IOException;

public abstract class KoanFileCopying implements ArgumentBehavior{

	@Override
	public void run(String value) {
		File baseDirectoryFile = new File(ClassLoader.getSystemResource(".").getFile()).getParentFile();  
		String baseDirectory = baseDirectoryFile.getAbsolutePath();
		String backupSrcDirectory = new StringBuilder(baseDirectory).append(FILESYSTEM_SEPARATOR)
																 .append("data")
																 .append(FILESYSTEM_SEPARATOR)
																 .append("src")
																 .append(FILESYSTEM_SEPARATOR).toString();
		String appSrcDirectory = new StringBuilder(baseDirectory).append(FILESYSTEM_SEPARATOR)
																	  .append("src")
																	  .append(FILESYSTEM_SEPARATOR).toString();
		try {
			copy(backupSrcDirectory, appSrcDirectory);
		} catch (IOException e) {
			System.out.println(getErrorMessage());
			System.exit(-1);
		}
		System.out.println(getSuccessMessage());
	}

	protected abstract String getSuccessMessage();
	protected abstract String getErrorMessage();
	protected abstract void copy(String backupSrcDirectory, String appSrcDirectory) throws IOException;  
	
}

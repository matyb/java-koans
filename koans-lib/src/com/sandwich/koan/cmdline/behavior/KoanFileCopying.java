package com.sandwich.koan.cmdline.behavior;

import static com.sandwich.koan.constant.KoanConstants.FILESYSTEM_SEPARATOR;

import java.io.IOException;

public abstract class KoanFileCopying extends AbstractArgumentBehavior {

	private final String koans = "koans";
	private final String src = "src";
	
	public void run(String value) {
		try {
			copy(new StringBuilder(koans).append(FILESYSTEM_SEPARATOR).append("data").append(FILESYSTEM_SEPARATOR).append(src).toString(), 
				 new StringBuilder(koans).append(FILESYSTEM_SEPARATOR).append(src).toString());
		} catch (IOException e) {
			System.out.println(getErrorMessage());
			System.exit(-1);
		}
		System.out.println(getSuccessMessage());
	}

	protected abstract void copy(String backupSrcDirectory, String appSrcDirectory) throws IOException;  
	
}

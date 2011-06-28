package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.io.directories.DirectoryManager;

public abstract class KoanFileCopying extends AbstractArgumentBehavior {

	public void run(String value) {
		try {
			copy(DirectoryManager.getProjectDataSourceDir(), DirectoryManager.getSourceDir());
		} catch (IOException e) {
			System.out.println(getErrorMessage());
			System.exit(-1);
		}
		System.out.println(getSuccessMessage());
	}

	protected abstract void copy(String backupSrcDirectory, String appSrcDirectory) throws IOException;  
	
}

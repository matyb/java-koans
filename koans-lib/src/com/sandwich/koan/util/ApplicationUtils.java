package com.sandwich.koan.util;

import java.io.File;

import com.sandwich.util.io.directories.DirectoryManager;

public class ApplicationUtils {

	static public boolean isFirstTimeAppHasBeenRun() {
		File dataDirectory = new File(DirectoryManager.getDataDir());
		return !dataDirectory.exists();
	}
	
}

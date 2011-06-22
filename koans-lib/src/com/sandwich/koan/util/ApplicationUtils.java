package com.sandwich.koan.util;

import static com.sandwich.koan.constant.KoanConstants.DATA_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.FILESYSTEM_SEPARATOR;
import static com.sandwich.koan.constant.KoanConstants.PROJ_MAIN_FOLDER;

import java.io.File;

import com.sandwich.util.io.FileUtils;

public class ApplicationUtils {

	static public boolean isFirstTimeAppHasBeenRun() {
		File dataDirectory = new File(FileUtils.makeAbsoluteRelativeTo(
				new StringBuilder(PROJ_MAIN_FOLDER).append(FILESYSTEM_SEPARATOR).append(DATA_FOLDER).toString()));
		return !dataDirectory.exists();
	}
	
}

package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.FileUtils;

public class Backup extends KoanFileCopying{

	@Override
	protected void copy(String backupSrcDirectory, String appSrcDirectory)
			throws IOException {
		FileUtils.copy(appSrcDirectory, backupSrcDirectory);
	}
	
	@Override
	protected String getErrorMessage() {
		return "An issue was encountered saving a backup copy. Check that the directory exists and try again.";
	}

	@Override
	protected String getSuccessMessage() {
		return "Working koans were backed up successfully";
	}
	
}

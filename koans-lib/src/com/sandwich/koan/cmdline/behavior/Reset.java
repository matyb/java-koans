package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.FileUtils;

public class Reset extends KoanFileCopying{

	@Override
	protected void copy(String backupSrcDirectory, String appSrcDirectory)
			throws IOException {
		FileUtils.copy(backupSrcDirectory, appSrcDirectory);
	}
	
	@Override
	protected String getErrorMessage() {
		return  "There was an unanticipated error encountered restoring the koan files. " +
				"You're best bet is to start with a fresh copy from your downloads.";
	}
	
	@Override
	protected String getSuccessMessage() {
		return "Koans restored successfully.";
	}
	
}

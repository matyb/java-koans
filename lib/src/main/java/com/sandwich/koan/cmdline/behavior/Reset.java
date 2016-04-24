package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.io.CopyFileOperation;

public class Reset extends KoanFileCopying {

	@Override
	protected void copy(String backupSrcDirectory, String appSrcDirectory)
			throws IOException {
		new CopyFileOperation(backupSrcDirectory, appSrcDirectory).operate();
	}
	
}

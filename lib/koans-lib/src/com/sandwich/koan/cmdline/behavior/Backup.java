package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.io.FileUtils;

public class Backup extends KoanFileCopying{

	@Override
	protected void copy(String backupSrcDirectory, String appSrcDirectory)
			throws IOException {
		FileUtils.copy(appSrcDirectory, backupSrcDirectory);
	}
	
}

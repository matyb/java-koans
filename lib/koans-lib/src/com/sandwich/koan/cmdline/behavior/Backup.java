package com.sandwich.koan.cmdline.behavior;

import java.io.File;
import java.io.IOException;

import com.sandwich.util.io.CopyFileOperation;

public class Backup extends KoanFileCopying{

	@Override
	protected void copy(String backupSrcDirectory, String appSrcDirectory)
			throws IOException {
		File backupDir = new File(backupSrcDirectory);
		if(!backupDir.exists()){
			backupDir.mkdirs();
		}
		File sourceDir = new File(appSrcDirectory);
		new CopyFileOperation(sourceDir, backupDir){
			public void onNew(File file) throws IOException {
				file.mkdirs();
			};
		}.operate();
	}
	
}

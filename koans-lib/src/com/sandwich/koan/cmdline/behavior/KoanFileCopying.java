package com.sandwich.koan.cmdline.behavior;

import static com.sandwich.koan.constant.KoanConstants.FILESYSTEM_SEPARATOR;
import static com.sandwich.koan.constant.KoanConstants.PROJ_MAIN_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.SOURCE_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.DATA_FOLDER;


import java.io.IOException;

public abstract class KoanFileCopying extends AbstractArgumentBehavior {

	public void run(String value) {
		try {
			copy(new StringBuilder(	PROJ_MAIN_FOLDER).append(FILESYSTEM_SEPARATOR).append(DATA_FOLDER)
							.append(FILESYSTEM_SEPARATOR).append(SOURCE_FOLDER).toString(), 
				 new StringBuilder(PROJ_MAIN_FOLDER).append(FILESYSTEM_SEPARATOR).append(SOURCE_FOLDER).toString());
		} catch (IOException e) {
			System.out.println(getErrorMessage());
			System.exit(-1);
		}
		System.out.println(getSuccessMessage());
	}

	protected abstract void copy(String backupSrcDirectory, String appSrcDirectory) throws IOException;  
	
}

package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

public abstract class ExistingFileAction extends FileOperation {

	public ExistingFileAction(String... strings) {
		super(strings);
	}

	public void onNull(File file) throws IOException {
		throwNonExistentFileError(String.valueOf(file));
	}
	
	public void onNew(File file) throws IOException {
		throwNonExistentFileError(file.getAbsolutePath());
	}
	
	private String throwNonExistentFileError(String path) {
		throw new IllegalArgumentException("Source path must actually exist. ("+ path +")");
	}
	
}

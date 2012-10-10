package com.sandwich.util.io;

import java.io.File;

public interface FileListener {

	void fileSaved(File file);
	void newFile(File file);
	void fileDeleted(File file);
	
}

package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

public interface FileAction {

	void onFile(File file) throws IOException;
	void onDirectory(File dir) throws IOException;
	void onNull(File nullFile) throws IOException;
	void onNew(File newFile) throws IOException;
	
}

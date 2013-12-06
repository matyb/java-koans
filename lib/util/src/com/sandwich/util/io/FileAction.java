package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

public interface FileAction {

	void sourceToDestination(File src, File dest) throws IOException;
	
}

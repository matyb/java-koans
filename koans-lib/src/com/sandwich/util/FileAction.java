package com.sandwich.util;

import java.io.File;
import java.io.IOException;

public interface FileAction {

	void sourceToDestination(File src, File dest) throws IOException;

	File makeDestination(File dest, String fileInDirectory);
	
}

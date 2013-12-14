package com.sandwich.koan.cmdline.behavior;

import java.io.File;

import com.sandwich.util.io.directories.DirectoryManager;

public class Clear extends AbstractArgumentBehavior {

	public void run(String... values) throws Exception {
		new File(DirectoryManager.getDataFile()).delete();
		new File(DirectoryManager.getBinDir()).delete();
		new File(DirectoryManager.getDataDir()).delete(); 
	}
	
}

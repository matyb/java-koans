package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.io.FileCompiler;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.directories.Production;
import com.sandwich.util.io.directories.UnitTest;

public class Test extends AbstractArgumentBehavior{

	public void run(String value) throws IOException {
		try{
			DirectoryManager.setDirectorySet(new UnitTest());
			FileCompiler.compile(DirectoryManager.getSourceDir(), DirectoryManager.getBinDir());
		}finally{
			DirectoryManager.setDirectorySet(new Production());
		}
	}
	
}

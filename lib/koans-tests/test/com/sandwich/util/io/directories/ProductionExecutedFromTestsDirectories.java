package com.sandwich.util.io.directories;

public class ProductionExecutedFromTestsDirectories extends ProductionDirectories {

	@Override
	public String getBaseDir() {
		String baseDir = super.getBaseDir();
		return baseDir.substring(0, baseDir.length() - getLibrariesDir().length() - 1);
	}
	
}

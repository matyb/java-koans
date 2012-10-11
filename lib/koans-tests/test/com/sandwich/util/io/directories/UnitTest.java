package com.sandwich.util.io.directories;


public class UnitTest extends DirectorySet {

	public String getSourceDir() {
		return "test";
	}

	public String getProjectDir() {
		return super.getLibrariesDir() + System.getProperty("file.separator")
				+ "koans-tests";
	}

}

package com.sandwich.util.io.directories;



public class UnitTestDirectories extends ProductionExecutedFromTestsDirectories {
	
	public String getSourceDir() {
		return "test";
	}
	
	public String getProjectDir() {
		return super.getLibrariesDir() + System.getProperty("file.separator")
				+ "koans-tests";
	}

}

package com.sandwich.util.io.directories;



public class UnitTestDirectories extends ProductionDirectories {
	
	public String getSourceDir() {
		return "java";
	}
	
	public String getProjectDir() {
		String sep = System.getProperty("file.separator");
		return super.getLibrariesDir() + sep
				+ "src" + sep + "test";
	}

}

package com.sandwich.util.io.directories;

import java.io.File;

abstract public class DirectorySet {

	private static final String BIN_DIR 		= "bin";
	private static final String APP_DIR 		= "app";
	private static final String LIB_DIR 		= "lib";
	private static final String DATA_DIR	 	= "data";
	private static final String I18N_DIR		= "i18n";
	private static final String CONFIG_DIR		= "config";
	private static final String BASE_DIR 		= createBaseDir();
	
	abstract String getSourceDir();
	abstract String getProjectDir();
	
	public String getBaseDir(){
		return BASE_DIR;
	}
	
	public String getBinaryDir(){
		return BIN_DIR;
	}
	
	public String getLibrariesDir(){
		return LIB_DIR;
	}
	
	public String getI18nDir(){
		return I18N_DIR;
	}
	
	public String getAppDir(){
		return APP_DIR;
	}
	
	public String getConfigDir() {
		return CONFIG_DIR;
	}
	
	public String getDataDir(){
		return DATA_DIR;
	}
	
	private static String createBaseDir() {
		String baseDir = System.getProperty("application.basedir");
		if(baseDir != null){
			if(baseDir.startsWith("\"")){
				baseDir = baseDir.substring(1);
			}
			if(baseDir.endsWith("\"")){
				baseDir = baseDir.substring(0, baseDir.length() - 1);
			}
			return new File(baseDir).getParentFile().getAbsolutePath();
		}
		return new File("").getAbsoluteFile().getParentFile().getAbsolutePath();
	}
}

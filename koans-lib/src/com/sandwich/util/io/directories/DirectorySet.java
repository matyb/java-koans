package com.sandwich.util.io.directories;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import com.sandwich.koan.util.ApplicationUtils;

abstract class DirectorySet {

	private static final String BIN_DIR 		= "bin";
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
	
	public String getConfigDir() {
		return CONFIG_DIR;
	}
	
	public String getDataDir(){
		return DATA_DIR;
	}
	
	private static String createBaseDir() {
		URL path = DirectorySet.class.getClassLoader().getResource(".");
		try {
			File dir = new File(path.toURI());
			if(ApplicationUtils.isWindows()){
				dir = new File(dir.getAbsolutePath().replace("%20", " "));
			}
			if (dir.exists()) {
				dir = dir.getParentFile();
				if (dir.exists()) {
					dir = dir.getParentFile();
				}
			}
			return dir.getAbsolutePath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}

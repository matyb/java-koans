package com.sandwich.util.io.directories;


public abstract class DirectoryManager {
	private DirectoryManager(){}
/*
 * FileUtils, IOConstants, KoanConsants, FileCompiler
	public static String 		PROJ_MAIN_FOLDER 	= "koans";
	public static String 		PROJ_TESTS_FOLDER 	= "koans-tests";

	public static String 		SOURCE_FOLDER 		= "src";
	public static String 		BIN_FOLDER 			= "bin";
	public static String 		DATA_FOLDER 		= "data";
	public static String 		FILE_HASH_FILE_NAME = "file_hashes.dat";
	public static String 		TESTS_FOLDER 		= "test";
	
	
	public static final String FILESYSTEM_SEPARATOR	= System.getProperty("file.separator");
*/
	private static DirectorySet production = new Production();
	private static DirectorySet instance = production;
	
	public static final String FILESYSTEM_SEPARATOR	= System.getProperty("file.separator");
	
	public static void setDirectorySet(DirectorySet lInstance){
		instance = lInstance;
	}
	
	public static String getMainDir(){
		return constructMainDir(instance);
	}
	
	public static String getProdMainDir() {
		return constructMainDir(production);
	}
	
	private static String constructMainDir(DirectorySet directories){
		return injectFileSystemSeparators(	directories.getBaseDir(),
											directories.getProjectDir());
	}
	
	public static String getSourceDir(){
		return constructProjectDir(instance, instance.getSourceDir());
	}
	
	public static String getProdSourceDir(){
		return constructProjectDir(production, production.getSourceDir());
	}
	
	public static String getBinDir(){
		return constructProjectDir(instance, instance.getBinaryDir());
	}
	
	public static String getDataDir() {
		return constructProjectDir(instance, instance.getDataDir());
	}
	
	private static String constructProjectDir(DirectorySet directories, String childDir){
		return injectFileSystemSeparators(	directories.getBaseDir(), 
											directories.getProjectDir(), 
											childDir);
	}
	
	public static String getProjectLibraryDir(){
		return injectFileSystemSeparators(	instance.getBaseDir(), 
											production.getProjectDir(), 
											instance.getLibrariesDir());
	}
	
	public static String getProjectDataSourceDir() {
		return injectFileSystemSeparators(	getDataDir(),
											instance.getSourceDir());
	}
	
	/**
	 * Will take "home", "wilford", "liberty" and produce: "/home/wilford/liberty"
	 * 
	 * @param folders
	 * @return concatenated folders w/ file separators
	 */
	public static String injectFileSystemSeparators(String...folders){
		StringBuilder builder = new StringBuilder();
		for(String folder : folders){
			if(FILESYSTEM_SEPARATOR.equals(folder)){
				continue;
			}
			builder.append(FILESYSTEM_SEPARATOR);
			while(folder.startsWith(FILESYSTEM_SEPARATOR)){
				folder = folder.substring(1);
			}
			while(folder.endsWith(FILESYSTEM_SEPARATOR)){
				folder = folder.substring(0, folder.lastIndexOf(FILESYSTEM_SEPARATOR));
			}
			builder.append(folder);
		}
		return builder.toString();
	}
	
}

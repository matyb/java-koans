package com.sandwich.util.io.directories;

import com.sandwich.koan.util.ApplicationUtils;


public abstract class DirectoryManager {
	
	private DirectoryManager(){}
	
	private static DirectorySet production = new ProductionDirectories();
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
	
	public static String getConfigDir(){
		return injectFileSystemSeparators(	instance.getBaseDir(),
											production.getProjectDir(),
											instance.getAppDir(),
											instance.getConfigDir());
	}
	
	public static String getSourceDir(){
		return constructProjectDir(instance, instance.getSourceDir());
	}
	
	public static String getProdSourceDir(){
		return constructProjectDir(production, production.getSourceDir());
	}
	
	public static String getBinDir(){
		return injectFileSystemSeparators(  instance.getBaseDir(),
											production.getProjectDir(),
											instance.getAppDir(),
											instance.getBinaryDir());
	}
	
	public static String getDataDir() {
		return injectFileSystemSeparators(	instance.getBaseDir(), 
											production.getProjectDir(),
											instance.getAppDir(), 
											instance.getDataDir());
	}
	
	private static String constructProjectDir(DirectorySet directories, String childDir){
		return injectFileSystemSeparators(	directories.getBaseDir(), 
											directories.getProjectDir(), 
											childDir);
	}
	
	public static String getProjectLibraryDir(){
		return injectFileSystemSeparators(	instance.getBaseDir(), 
											production.getProjectDir(),
											instance.getAppDir(),
											instance.getLibrariesDir());
	}
	
	public static String getProjectI18nDir(){
		return injectFileSystemSeparators(	instance.getBaseDir(),
											production.getProjectDir(),
											instance.getAppDir(),
											instance.getConfigDir(), 
											instance.getI18nDir());
	}
	
	public static String getProjectDataSourceDir() {
		return injectFileSystemSeparators(getDataDir(), instance.getSourceDir());
	}
	
	public static String getDataFile() {
		return injectFileSystemSeparators(getConfigDir(), "file_hashes.dat");
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
		String constructedFolder = builder.toString();
		if(ApplicationUtils.isWindows()){
			if(constructedFolder.startsWith(FILESYSTEM_SEPARATOR)){
				constructedFolder = constructedFolder.substring(1);
			}
			constructedFolder = constructedFolder.replace("%20", " ");
		}
		return constructedFolder;
	}
	
}

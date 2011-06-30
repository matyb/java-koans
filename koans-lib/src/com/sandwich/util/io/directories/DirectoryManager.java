package com.sandwich.util.io.directories;

import com.sandwich.koan.util.ApplicationUtils;


public abstract class DirectoryManager {
	
	private DirectoryManager(){}
	
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
		return injectFileSystemSeparators(	instance.getBaseDir(), 
											production.getProjectDir(), 
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
		String constructedFolder = builder.toString();
		if(System.getProperty("os.name").toLowerCase().contains("win") && constructedFolder.startsWith(FILESYSTEM_SEPARATOR)){
			constructedFolder = constructedFolder.substring(1);
		}
		if(ApplicationUtils.isWindows() && constructedFolder.contains("%20")){
				constructedFolder.replace("%20", " ");
		}
		return constructedFolder;
	}
	
}

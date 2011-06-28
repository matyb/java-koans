package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.sandwich.util.io.directories.DirectoryManager;

public class FileMonitor {

	public static String FILE_HASH_FILE_NAME = "file_hashes.dat";
	
	private final List<FileListener> listeners = new Vector<FileListener>();
	private static DataFileHelper<Map<String, Long>> fileHashesHelper =  new DataFileHelper<Map<String,Long>>(
											getFileHashesDataFilePath(), new HashMap<String,Long>());
	
	private static Map<String, Long> fileHashesByDirectory = fileHashesHelper.retrieve();
	
	final File fileSystemPath;
	
	public FileMonitor(String fileSystemPath){
		this.fileSystemPath = new File(fileSystemPath);
		if(!this.fileSystemPath.exists()){
			throw new IllegalArgumentException(fileSystemPath+ " cannot be found.");
		}
	}
	
	public String getFilesystemPath() {
		return fileSystemPath.getAbsolutePath();
	}
	
	public void close(){
		listeners.clear();
	}

	public void addFileSavedListener(FileListener listener){
		listeners.add(listener);
	}
	
	public void removeFileSavedListener(FileListener listener){
		listeners.remove(listener);
	}
	
	synchronized void notifyListeners(){
		try {
			Map<String, Long> currentHashes = getFilesystemHashes();
			for(String fileName : currentHashes.keySet()){
				Long currentHash 	= currentHashes.get(fileName);
				Long previousHash 	= fileHashesByDirectory.get(fileName);
				if(previousHash != null && !currentHash.equals(previousHash)){
					fileHashesByDirectory.put(fileName, currentHash);
					File file = new File(fileName);
					for(FileListener listener : listeners){
						listener.fileSaved(file);
					}
					break;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	Map<String, Long> getFilesystemHashes() throws IOException {
		final HashMap<String,Long> fileHashes = new HashMap<String,Long>();
		FileUtils.forEachFile(fileSystemPath, fileSystemPath, new FileAction(){
			public File makeDestination(File dest, String fileInDirectory) {
				return new File(dest, fileInDirectory);
			}
			public void sourceToDestination(File src, File dest) throws IOException {
				fileHashes.put(src.getAbsolutePath(), src.lastModified());
			}
		});
		return fileHashes;
	}
	
	private static String getFileHashesDataFilePath(){
		return new StringBuilder(DirectoryManager.getDataDir())
					.append(System.getProperty("file.separator")).append(FILE_HASH_FILE_NAME).toString();
	}

	public boolean isFileModifiedSinceLastPoll(String filePath, Long lastModified) {
		Long previousHash = fileHashesByDirectory.get(filePath);
		return !lastModified.equals(previousHash);
	}

	public void updateFileSaveTime(File file) {
		fileHashesByDirectory.put(file.getAbsolutePath(), file.lastModified());
	}
	
}

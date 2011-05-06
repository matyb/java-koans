package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class FileMonitor {

	private final List<FileListener> listeners = new Vector<FileListener>();
	private static Map<String, Long> fileHashesByDirectory = new HashMap<String, Long>();
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
		FileMonitorFactory.removeInstance(fileSystemPath.getAbsolutePath());
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
			if(fileHashesByDirectory.isEmpty()){
				fileHashesByDirectory.putAll(currentHashes);
			}
			for(String fileName : currentHashes.keySet()){
				Long currentHash 	= currentHashes.get(fileName);
				Long previousHash 	= fileHashesByDirectory.get(fileName);
				if(!currentHash.equals(previousHash)){
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
	
}

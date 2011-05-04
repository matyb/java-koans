package com.sandwich.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class FileMonitor {

	final File fileSystemPath;
	final static List<FileMonitor> monitors = new Vector<FileMonitor>();
	private long lastPolled;
	
	private static List<FileSavedListener> listeners = new Vector<FileSavedListener>();
	static{ 
		new Thread(new Runnable(){
			public void run() {
				do{
					long now = System.currentTimeMillis();
					for(FileMonitor monitor : monitors){
						if(now - monitor.getLastPolled() > monitor.getPollingTimeInMillis()){
							monitor.notifyListeners();
							monitor.setLastPolled(now);
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}while(true);
			}
		}).start();
	}
	
	private Map<String, Long> fileHashesByDirectory = new HashMap<String, Long>();
	
	final long pollingTimeInMillis;
	Long lastPoll = 0l;
	Long hash = 0l;
	
	public static FileMonitor getInstance(String fileSystemPath, long pollingTimeInMillis) throws IOException{
		FileMonitor monitor = new FileMonitor(fileSystemPath, pollingTimeInMillis);
		monitors.add(monitor);
		return monitor;
	}
	
	public static void removeInstance(FileMonitor monitor){
		monitor.close();
	}
	
	public void close(){
		monitors.remove(this);
		listeners.clear();
	}
	
	private FileMonitor(String fileSystemPath, long pollingTimeInMillis) throws IOException{
		this.fileSystemPath = new File(fileSystemPath);
		this.pollingTimeInMillis = Math.abs(pollingTimeInMillis);
		reset();
	}

	public void reset() throws IOException{
		if(!fileSystemPath.isDirectory() || !fileSystemPath.exists()){
			throw new FileNotFoundException(fileSystemPath+" does not exist, or is not a directory");
		}
		Map<String, Long> hashes = getFilesystemHashes();
		fileHashesByDirectory = hashes;
		hash = hashEntries(hashes);
	}

	public void addFileSavedListener(FileSavedListener listener){
		listeners.add(listener);
	}
	
	public void removeFileSavedListener(FileSavedListener listener){
		listeners.remove(listener);
	}
	
	public long getPollingTimeInMillis(){
		return pollingTimeInMillis;
	}
	
	public long getLastPolled() {
		return lastPolled;
	}

	public void setLastPolled(long lastPolled) {
		this.lastPolled = lastPolled;
	}

	private void notifyListeners(){
		try {
			Map<String, Long> currentHashes = getFilesystemHashes();
			for(String fileName : currentHashes.keySet()){
				Long currentHash = currentHashes.get(fileName);
				if(!currentHash.equals(fileHashesByDirectory.get(fileName))){
					fileHashesByDirectory.put(fileName, currentHash);
					hash = hashEntries(currentHashes);
					File file = new File(fileName);
					for(FileSavedListener listener : listeners){
						listener.fileSaved(file);
					}
					break;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Long hashEntries(Map<String, Long> hashes) {
		Long hashTally = Long.valueOf(0);
		for(Long hash : hashes.values()){
			hashTally += hash;
		}
		return hashTally;
	}


	public boolean hasChanged() throws IOException{
		return !hash.equals(hashEntries(getFilesystemHashes()));
	}
	
	private Map<String, Long> getFilesystemHashes() throws IOException {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * hash);
		result = prime
				* result
				+ ((fileHashesByDirectory == null) ? 0 : fileHashesByDirectory
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileMonitor other = (FileMonitor) obj;
		if (!hash.equals(other.hash))
			return false;
		if (fileHashesByDirectory == null) {
			if (other.fileHashesByDirectory != null)
				return false;
		} else if (!fileHashesByDirectory.equals(other.fileHashesByDirectory))
			return false;
		return true;
	}
	
}

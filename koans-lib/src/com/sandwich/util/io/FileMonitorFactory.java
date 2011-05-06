package com.sandwich.util.io;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class FileMonitorFactory {

	private static Map<String, FileMonitor> monitors = new LinkedHashMap<String, FileMonitor>();
	public static final int SLEEP_TIME_IN_MS = 500;
	
	private static List<FileListener> listeners = new Vector<FileListener>();
	static{ 
		new Thread(new Runnable(){
			public void run() {
				do{
					try {
						Thread.sleep(SLEEP_TIME_IN_MS);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					for(Entry<String, FileMonitor> filePathAndMonitor : monitors.entrySet()){
						FileMonitor monitor = filePathAndMonitor.getValue();
						if(monitor != null){
							monitor.notifyListeners();
						}
					}
				}while(true);
			}
		}).start();
	}
	
	public static FileMonitor getInstance(String fileSystemPath) throws IOException{
		FileMonitor monitor = monitors.get(fileSystemPath);
		if(monitor == null){
			monitor = new FileMonitor(fileSystemPath);
			monitors.put(fileSystemPath, monitor);
		}
		return monitor;
	}
	
	public void close(){
		listeners.clear();
	}

	public static void removeInstance(FileMonitor monitor){
		monitors.remove(monitor.getFilesystemPath());
	}
	
	public static void removeInstance(String absolutePath) {
		monitors.remove(absolutePath);
	}

}

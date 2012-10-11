package com.sandwich.util.io;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FileMonitorFactory {

	private static Map<String, FileMonitor> monitors = new LinkedHashMap<String, FileMonitor>();
	public static final int SLEEP_TIME_IN_MS = 500;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run() {
				for(FileMonitor monitor : monitors.values()){
					monitor.close();
				}
				monitors.clear();
			}
		}));
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
	
	public static FileMonitor getInstance(File monitoredFile, File dataFile) {
		String key = monitoredFile.getAbsolutePath() + dataFile.getAbsolutePath();
		FileMonitor monitor = monitors.get(key);
		if(monitor == null){
			monitor = new FileMonitor(monitoredFile, dataFile);
			monitors.put(key, monitor);
		}
		return monitor;
	}

	public static void removeInstance(FileMonitor monitor){
		monitor.close();
		monitors.remove(monitor.getFilesystemPath());
	}
	
	public static void removeInstance(String absolutePath) {
		monitors.remove(absolutePath);
	}

	public static void closeAll() {
		for(FileMonitor monitor : monitors.values()){
			monitor.close();
		}
		monitors.clear();
	}

}

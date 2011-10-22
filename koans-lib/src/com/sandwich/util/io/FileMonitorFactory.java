package com.sandwich.util.io;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.constant.ApplicationSettings;

public class FileMonitorFactory {

	private static Map<String, FileMonitor> monitors = new LinkedHashMap<String, FileMonitor>();
	public static final int SLEEP_TIME_IN_MS = 500;
	private static boolean keepCheckingDirectory = true;
	
	// poll for file modifications
	static{
		new Thread(new Runnable(){
			public void run() {
				while(keepCheckingDirectory){
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
				}
			}
		}).start();
	}
	
	// poll for keyboard input to stop the polling
	static{
		new Thread(new Runnable(){
			public void run() {
				while(keepCheckingDirectory){
					char c = ' ';
					try {
						c = (char)System.in.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(Character.toUpperCase(c) == Character.valueOf(ApplicationSettings.getExitChar())){
						keepCheckingDirectory = false;
						for(FileMonitor monitor : monitors.values()){
							monitor.close();
						}
						monitors.clear();
					}
				}
			}
		}).start();
	}
	
	public static FileMonitor getInstance(String fileSystemPath) {
		FileMonitor monitor = monitors.get(fileSystemPath);
		if(monitor == null){
			monitor = new FileMonitor(fileSystemPath);
			monitors.put(fileSystemPath, monitor);
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

}

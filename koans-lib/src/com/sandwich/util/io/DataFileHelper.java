package com.sandwich.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles persistence to/from a filesystem, and makes assumption instances of T
 * are mutated after retrieval. Their mutations are saved to file on app
 * shutdown automatically.
 * 
 * @param <T>
 */
public class DataFileHelper<T> {

	private File dataFile;
	private T lastRetrieval;
	
	public DataFileHelper(String absolutePath, T defaultState){
		dataFile = new File(absolutePath);
		if(!dataFile.exists()){
			save(defaultState);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if(lastRetrieval != null){
					save(lastRetrieval);
				}
	        }
	    }));

	}
	
	@SuppressWarnings("unchecked")
	public T retrieve(){
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(getDataFile()));
			return lastRetrieval = (T)objectInputStream.readObject();
		} catch (Exception e) {
			try{
				if(objectInputStream != null){
					objectInputStream.close();
				}
			}catch(Exception e2){
				throw new RuntimeException(e2);
			}
			throw new RuntimeException(e);
		}
	}

	public void save(T state){
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(getDataFile()));
			objectOutputStream.writeObject(state);
		} catch (Exception e) {
			try{
				if(objectOutputStream != null){
					objectOutputStream.close();
				}
			}catch(Exception e2){
				throw new RuntimeException(e2);
			}
			throw new RuntimeException(e);
		}
	}
	
	private File getDataFile() {
		return dataFile;
	}
	
}

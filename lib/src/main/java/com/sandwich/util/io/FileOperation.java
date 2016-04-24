package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

public abstract class FileOperation implements FileAction {

	public void operate(File file) throws IOException {
		if(file == null){
			onNull(file);
		}else if(!file.exists()){
			onNew(file);
		}else if(file.isDirectory()){
			onDirectory(file);
		}else{
			onFile(file);
		}
	}

}

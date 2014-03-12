package com.sandwich.koan.cmdline.behavior;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.directories.DirectoryManager;

public class Clear extends AbstractArgumentBehavior {

	List<String> files = Arrays.asList(
			DirectoryManager.getDataFile(),
			DirectoryManager.getBinDir(),
			DirectoryManager.getDataDir());
	
	public void run(String... values) throws Exception {
		List<File> unableToDelete = new ArrayList<File>();
		for(String fileName : files){
			File file = new File(fileName);
			if(file.exists()){
				if(file.delete()){
					ApplicationUtils.getPresenter().displayMessage(file.getAbsolutePath() + " deleted successfully.");
				}else{
					unableToDelete.add(file);
					ApplicationUtils.getPresenter().displayError(file.getAbsolutePath() + " was NOT DELETED. Please delete manually.");
				}
			}else{
				ApplicationUtils.getPresenter().displayMessage(file.getAbsolutePath() + " does not exist. Skipping.");
			}
		}
		if(!unableToDelete.isEmpty()){
			throw new RuntimeException("Unable to delete: "+unableToDelete+" see output for details.");
		}
	}
	
}

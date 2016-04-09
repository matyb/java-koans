package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.directories.DirectoryManager;

public abstract class KoanFileCopying extends AbstractArgumentBehavior {

	public void run(String... values) {
		SuitePresenter presenter = ApplicationUtils.getPresenter();
		try {
			copy(DirectoryManager.getProjectDataSourceDir(), DirectoryManager.getSourceDir());
		} catch (IOException e) {
			e.printStackTrace();
			presenter.displayError(getErrorMessage());
			System.exit(-1);
		}
		presenter.displayMessage(getSuccessMessage());
	}

	protected abstract void copy(String backupSrcDirectory, String appSrcDirectory) throws IOException;  
	
}

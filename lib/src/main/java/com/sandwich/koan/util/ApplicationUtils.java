package com.sandwich.koan.util;

import java.io.File;

import com.sandwich.koan.ui.ConsolePresenter;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.util.io.directories.DirectoryManager;

public class ApplicationUtils {

	private static SuitePresenterFactory suitePresenterFactory = new SuitePresenterFactory();
	
	static public boolean isFirstTimeAppHasBeenRun() {
		File dataDirectory = new File(DirectoryManager.getDataDir());
		return !dataDirectory.exists();
	}
	
	static public boolean isWindows(){
		return System.getProperty("os.name").toLowerCase().contains("win");
	}

	static public SuitePresenter getPresenter(){
		return suitePresenterFactory.create();
	}
	
	public static class SuitePresenterFactory {
		protected SuitePresenter create(){
			return new ConsolePresenter();
		}
	}
}

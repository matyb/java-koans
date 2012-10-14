package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.directories.DirectoryManager;

public class KoanFileCompileAndRunListener implements FileListener {
	
	private Map<ArgumentType, CommandLineArgument> args = Collections.emptyMap();
	private KoanSuiteCompilationListener listener = new KoanSuiteCompilationListener();
	
	public KoanFileCompileAndRunListener(Map<ArgumentType, CommandLineArgument> args) throws IOException{
		this.args = args;
	}
	
	public void fileSaved(File file) {
		String absolutePath = file.getAbsolutePath();
		if(absolutePath.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)){
			ApplicationUtils.getPresenter().displayMessage(KoanConstants.EOL+"loading: "+absolutePath);
			try {
				FileCompiler.compile(file, 
					new File(DirectoryManager.getBinDir()),
					listener,
					ApplicationSettings.getFileCompilationTimeoutInMs(),
					new String[]{DirectoryManager.injectFileSystemSeparators(
							DirectoryManager.getProjectLibraryDir(), "koans.jar")});
				DynamicClassLoader.remove(FileCompiler.sourceToClass(
						DirectoryManager.getSourceDir(), DirectoryManager.getBinDir(), file).toURI().toURL());
				if(!listener.isLastCompilationAttemptFailure()){
					ApplicationUtils.getPresenter().clearMessages();
					new CommandLineArgumentRunner(args).run();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void newFile(File file) {
		
	}

	public void fileDeleted(File file) {
		
	}
}

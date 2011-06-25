package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.util.ConsoleUtils;

public class KoanFileCompileAndRunListener implements FileListener {
	
	private Map<ArgumentType, CommandLineArgument> args = Collections.emptyMap();
	
	public KoanFileCompileAndRunListener(Map<ArgumentType, CommandLineArgument> args) throws IOException{
		this.args = args;
	}
	
	public void fileSaved(File file) {
		String absolutePath = file.getAbsolutePath();
		if(absolutePath.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)){
			System.out.println(KoanConstants.EOL+"loading: "+absolutePath);
			try {
				FileCompiler.compile(file, new File(replaceSourceWithBin(absolutePath)), 
						FileUtils.makeAbsoluteRelativeTo(KoanConstants.PROJ_MAIN_FOLDER +
								KoanConstants.FILESYSTEM_SEPARATOR + KoanConstants.LIB_FOLDER + 
								KoanConstants.FILESYSTEM_SEPARATOR + "koans.jar"));
				DynamicClassLoader.remove(FileUtils.sourceToClass(file).toURI().toURL());
				ConsoleUtils.clearConsole();
				new CommandLineArgumentRunner(args).run();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private String replaceSourceWithBin(String absolutePath) {
		return new StringBuilder(absolutePath.substring(0, 
				absolutePath.lastIndexOf(KoanConstants.SOURCE_FOLDER))).append(
						KoanConstants.FILESYSTEM_SEPARATOR).append(
						KoanConstants.BIN_FOLDER).append(KoanConstants.FILESYSTEM_SEPARATOR).toString();
	}

	public void newFile(File file) {
		
	}

	public void fileDeleted(File file) {
		
	}
}

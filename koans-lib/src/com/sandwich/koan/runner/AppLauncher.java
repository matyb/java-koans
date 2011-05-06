package com.sandwich.koan.runner;

import java.util.Map;

import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.FileUtils;
import com.sandwich.util.io.KoanFileCompileAndRunListener;

public class AppLauncher {

	public static void main(final String... args) throws Throwable {
		Map<ArgumentType, CommandLineArgument> argsMap = new CommandLineArgumentBuilder(args);
		new KoanSuiteRunner(argsMap).run();
		if(KoanConstants.DEBUG){
			StringBuilder argsBuilder = new StringBuilder();
			int argNumber = 0;
			for(String arg : args){
				argsBuilder.append("Argument number "+String.valueOf(++argNumber)+": '"+arg+"'");
			}
			System.out.println(argsBuilder.toString());
		}
		if(argsMap.containsKey(ArgumentType.RUN_KOANS)){
			FileMonitorFactory.getInstance(FileUtils.makeAbsoluteRelativeTo(KoanConstants.PROJ_MAIN_FOLDER))
				.addFileSavedListener(new KoanFileCompileAndRunListener(argsMap));
		}
	}
}

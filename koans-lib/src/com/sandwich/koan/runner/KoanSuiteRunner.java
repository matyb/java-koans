package com.sandwich.koan.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.ArgumentType;

public class KoanSuiteRunner implements Runnable {

	private final Map<ArgumentType, CommandLineArgument> commandLineArguments;
	
	public KoanSuiteRunner(){
		this(new CommandLineArgumentBuilder());
	}
	
	public KoanSuiteRunner(Map<ArgumentType, CommandLineArgument> commandLineArguments) {
		this.commandLineArguments = Collections.unmodifiableMap(commandLineArguments);
	}

	public void run() {
		List<CommandLineArgument> sortedArguments = new ArrayList<CommandLineArgument>(commandLineArguments.values());
		Collections.sort(sortedArguments);
		for(CommandLineArgument argument : sortedArguments){
			argument.run();
		}
	}
	
}

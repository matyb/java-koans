package com.sandwich.koan.runner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.constant.KoanConstants;

public class KoanSuiteRunner {

	private final Map<ArgumentType, CommandLineArgument> commandLineArguments;
	
	@SuppressWarnings("unchecked")
	KoanSuiteRunner(){
		this(Collections.EMPTY_MAP);
	}
	
	public KoanSuiteRunner(
			Map<ArgumentType, CommandLineArgument> commandLineArguments) {
		this.commandLineArguments = Collections.unmodifiableMap(commandLineArguments);
	}

	public void run() throws ClassNotFoundException, IOException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Map<ArgumentType, CommandLineArgument> commandLineArguments = 
			new HashMap<ArgumentType, CommandLineArgument>(this.commandLineArguments); //clone entries
		ifNecessaryPlantDefaultArgumentToRunKoans(commandLineArguments);
		List<CommandLineArgument> sortedArguments = new ArrayList<CommandLineArgument>(commandLineArguments.values());
		Collections.sort(sortedArguments);
		for(CommandLineArgument argument : sortedArguments){
			argument.run();
		}
	}

	void ifNecessaryPlantDefaultArgumentToRunKoans(Map<ArgumentType, CommandLineArgument> commandLineArguments) {
		if(commandLineArguments.isEmpty() ||
				(!commandLineArguments.containsKey(ArgumentType.RUN_KOANS)
					&&	commandLineArguments.containsKey(ArgumentType.CLASS_ARG))){
			if(KoanConstants.DEBUG){
				System.out.println("Planting default run target.");
				for(Entry<ArgumentType, CommandLineArgument> argEntry : commandLineArguments.entrySet()){
					System.out.println("Key: '"+argEntry.getKey()+"'");
					System.out.println("Value: '"+argEntry.getValue()+"'");
				}
			}
			commandLineArguments.put(ArgumentType.RUN_KOANS, new CommandLineArgument(
					ArgumentType.RUN_KOANS, null));
		}
	}
	
}

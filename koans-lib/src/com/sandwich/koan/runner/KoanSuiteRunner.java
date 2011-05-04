package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.DATA_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.FILESYSTEM_SEPARATOR;
import static com.sandwich.koan.constant.KoanConstants.PROJ_MAIN_FOLDER;

import java.io.File;
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
import com.sandwich.util.io.FileUtils;

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
		applyAssumedStartupBehaviors(commandLineArguments);
		List<CommandLineArgument> sortedArguments = new ArrayList<CommandLineArgument>(commandLineArguments.values());
		Collections.sort(sortedArguments);
		for(CommandLineArgument argument : sortedArguments){
			argument.run();
		}
	}

	private boolean isFirstTimeAppHasBeenRun() {
		File dataDirectory = new File(FileUtils.makeAbsoluteRelativeTo(
				new StringBuilder(PROJ_MAIN_FOLDER).append(FILESYSTEM_SEPARATOR).append(DATA_FOLDER).toString()));
		return !dataDirectory.exists();
	}

	void applyAssumedStartupBehaviors(Map<ArgumentType, CommandLineArgument> commandLineArguments) {
		if(isFirstTimeAppHasBeenRun()){
			ArgumentType.BACKUP.run(null);
			// really hackish - but this will clear console for first run, not necessary after
			for(int i = 0; i < 80; i++){
				System.out.println();
			}
		}
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
					ArgumentType.RUN_KOANS, null, true));
		}
	}
	
}

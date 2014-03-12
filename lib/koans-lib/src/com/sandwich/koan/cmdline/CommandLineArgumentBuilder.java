package com.sandwich.koan.cmdline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;

public class CommandLineArgumentBuilder extends LinkedHashMap<ArgumentType, CommandLineArgument> {
	
	private static final long serialVersionUID = 7635285665311420603L;

	public CommandLineArgumentBuilder(String...args){
		args = filterOutNullElements(args);
		if(args.length == 0){
			put(ArgumentType.RUN_KOANS, new CommandLineArgument(ArgumentType.RUN_KOANS, new String[0]));
		} else if (args.length == 1 && ArgumentType.findTypeByString(args[0]) == null){
			put(ArgumentType.CLASS_ARG, new CommandLineArgument(ArgumentType.CLASS_ARG, args[0]));
		} else if (args.length == 2 && ArgumentType.findTypeByString(args[0]) == null && ArgumentType.findTypeByString(args[1]) == null){
			put(ArgumentType.CLASS_ARG, new CommandLineArgument(ArgumentType.CLASS_ARG, args[0]));
			put(ArgumentType.METHOD_ARG, new CommandLineArgument(ArgumentType.METHOD_ARG, args[1]));
		} else {
			ArgumentType type = null;
			List<String> params = null;
			for(int index = 0; index < args.length; index++){
				ArgumentType tmpType = ArgumentType.findTypeByString(args[index]);
				if(tmpType == null){
					if(type == null){
						Logger.getAnonymousLogger().warning("The argument: " + args[index] + " is not recognized, it will be ignored");
					}else{
						params.add(args[index]);
					}
				}else{
					if(type == null){
						type = tmpType;
						params = new ArrayList<String>();
					}else{
						put(type, new CommandLineArgument(type, params.toArray(new String[params.size()])));
						type = null;
						params = new ArrayList<String>(); 
					}
				}
			}
			if(type != null && params != null){
				put(type, new CommandLineArgument(type, params.toArray(new String[params.size()])));
			}
		}
		applyAssumedStartupBehaviors();
	}

	private String[] filterOutNullElements(String... args) {
		List<String> tempArgs = new ArrayList<String>();
		for(String arg : args){
			if(arg != null && arg.trim().length() > 0){
				tempArgs.add(arg.trim());
			}
		}
		return tempArgs.toArray(new String[tempArgs.size()]);
	}

	void applyAssumedStartupBehaviors() {
		if(ApplicationUtils.isFirstTimeAppHasBeenRun()){
			ArgumentType.BACKUP.run(new String[0]);
			ApplicationUtils.getPresenter().clearMessages();
		}
		if(isEmpty() || !containsKey(ArgumentType.RUN_KOANS) && (
				containsKey(ArgumentType.CLASS_ARG) ||
				containsKey(ArgumentType.DEBUG))){
			if(ApplicationSettings.isDebug()){
				SuitePresenter presenter = ApplicationUtils.getPresenter();
				presenter.displayMessage("Planting default run target.");
				for(Entry<ArgumentType, CommandLineArgument> argEntry : entrySet()){
					presenter.displayMessage("Key: '"+argEntry.getKey()+"'");
					presenter.displayMessage("Value: '"+argEntry.getValue()+"'");
				}
			}
			put(ArgumentType.RUN_KOANS, new CommandLineArgument(ArgumentType.RUN_KOANS, true, new String[0]));
		}
	}
}

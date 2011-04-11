package com.sandwich.koan.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sandwich.koan.cmdline.behavior.ArgumentBehavior;
import com.sandwich.koan.cmdline.behavior.Backup;
import com.sandwich.koan.cmdline.behavior.ClassArg;
import com.sandwich.koan.cmdline.behavior.Debug;
import com.sandwich.koan.cmdline.behavior.Help;
import com.sandwich.koan.cmdline.behavior.MethodArg;
import com.sandwich.koan.cmdline.behavior.Reset;
import com.sandwich.koan.cmdline.behavior.Test;
import com.sandwich.koan.runner.RunKoans;
import com.sandwich.util.Messages;

public enum ArgumentType implements ArgumentBehavior {	
	
	HELP(		getString("help_description"), 			new Help(), 		getCommandLineArgs("help_args")), 
	RESTORE(	getString("restore_description"), 		new Reset(), 		getCommandLineArgs("restore_args")), 
	BACKUP(		getString("backup_description"), 		new Backup(), 		getCommandLineArgs("backup_args")),
	DEBUG(		getString("debug_description"), 		new Debug(), 		getCommandLineArgs("debug_args")), 
	TEST(		getString("test_description"), 			new Test(), 		getCommandLineArgs("test_args")), 
	// important class MUST come before method - due to how Enum implements comparable and order
	// dependent logic later @ see ArgumentTypeTest.testClassPrecedesMethod
	CLASS_ARG(	getString("class_description"), 		new ClassArg(), 	getCommandLineArgs("class_args")),  
	METHOD_ARG(	getString("method_description"), 		new MethodArg(),	getCommandLineArgs("method_args")), 
	RUN_KOANS(	getString("default_run_description"), 	new RunKoans(), 	getCommandLineArgs("default_run_args"));  
	
	private final List<String> args;
	private final ArgumentBehavior behavior;
	private final String description;
	
	ArgumentType(String description, ArgumentBehavior behavior, String...args){
		this.args = Arrays.asList(args);
		this.behavior = behavior;
		this.description = description;
	}
	static String[] getCommandLineArgs(String key) {
		return Messages.getCSVs(ArgumentType.class, key);
	}
	private static String getString(String key) {
		return Messages.getString(ArgumentType.class, key);
	}
	public List<String> args(){
		return args;
	}
	public String description(){
		return description;
	}
	private static final Map<String, ArgumentType> TYPES_BY_STRING;
	static{
		Map<String, ArgumentType> types = new HashMap<String, ArgumentType>();
		for(ArgumentType type : ArgumentType.values()){
			for(String arg : type.args()){
				if(types.containsKey(arg)){
					throw new IllegalArgumentException(Messages.getString("ArgumentType.duplicated_arg_error_part1")+arg+Messages.getString("ArgumentType.duplicated_arg_error_part2")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				types.put(arg, type);
			}
		}
		TYPES_BY_STRING = Collections.unmodifiableMap(types);
	}
	public void run(String value) {
		behavior.run(value);
	}
	public static ArgumentType findTypeByString(String stringArg) {
		return TYPES_BY_STRING.get(stringArg);
	}
}



package com.sandwich.koan.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sandwich.koan.cmdline.behavior.ArgumentBehavior;
import com.sandwich.koan.cmdline.behavior.ClassArg;
import com.sandwich.koan.cmdline.behavior.Commit;
import com.sandwich.koan.cmdline.behavior.Deploy;
import com.sandwich.koan.cmdline.behavior.Help;
import com.sandwich.koan.cmdline.behavior.MethodArg;
import com.sandwich.koan.cmdline.behavior.Test;
import com.sandwich.koan.runner.RunKoans;

public enum ArgumentType implements ArgumentBehavior {	
	
//	COMPILE("-c", new Compile(), "Compiles the application."), // requires tools jar - wait till needed to require 
	TEST("Run tests. System returns number of failing testcases - not to exceed 255 to retain DOS compatibility for BAT files.", 
			new Test(), "-test"),
	// important class MUST come before method - due to how Enum implements comparable and order
	// dependent logic later @ see ArgumentTypeTest.testClassPrecedesMethod
	CLASS_ARG("Switch is optional, app tries to find a class definition for any unrecognized string - which becomes a method argument if class is not found. If classcast succeeds - the class will become the only koansuite to run. Permits users/developers to focus on one suite at a time.", 
			new ClassArg(), "-class"), 
	METHOD_ARG("Switch is optional, results from failing to find a class definition by an unrecognized string if switch is omitted.", 
			new MethodArg(), "-method"), 
	DEPLOY("Runs tests, if all pass - packs a jar for the koans folder, otherwise present stack trace from first failed test.",
			new Deploy(), "-deploy"),  
	COMMIT("Runs tests, if all pass - pushes all changes in lib, koans, and tests to branch (sring value following switch). If tests fail, present stack trace from first failed test.",
			new Commit(), "-commit"),  
	HELP("Help. Displays stuff to, er, help you.",
			new Help(), "-help", "help", "h", "?"),
	RUN_KOANS("Defaul targent. No switch - this runs if no switch is defined, or if a valid class is found as an argument.", 
			new RunKoans(), ""); 
	
	private final List<String> args;
	private final ArgumentBehavior behavior;
	private final String description;
	ArgumentType(String description, ArgumentBehavior behavior, String...args){
		this.args = Arrays.asList(args);
		this.behavior = behavior;
		this.description = description;
	}
	public List<String> args(){
		return args;
	}
	public String description(){
		return description;
	}
	public static final Map<String, ArgumentType> TYPES_BY_STRING;
	static{
		Map<String, ArgumentType> types = new HashMap<String, ArgumentType>();
		for(ArgumentType type : ArgumentType.values()){
			for(String arg : type.args){
				types.put(arg, type);
			}
		}
		TYPES_BY_STRING = Collections.unmodifiableMap(types);
	}
	@Override
	public void run(String value) {
		behavior.run(value);
	}
}



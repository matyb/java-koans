package com.sandwich.koan.cmdline;

import java.util.HashMap;
import java.util.Map;

import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.util.Builder;

public class CommandLineArgumentBuilder implements Builder<Map<ArgumentType, CommandLineArgument>>{
	private String[] args;
	public CommandLineArgumentBuilder(String...args){
		this.args = args;
	}
	@Override
	public Map<ArgumentType, CommandLineArgument> build() {
		Map<ArgumentType, CommandLineArgument> commandLineArguments = new HashMap<ArgumentType, CommandLineArgument>();
		if(args != null){
			for(int index = 0; index < args.length;){
				String stringArg = args[index];
				// is incremented in test of ternary
				String stringArgPlusOne = args.length <= ++index ? null : args[index]; 
				ArgumentType argumentType = ArgumentType.TYPES_BY_STRING.get(stringArg);
				ArgumentType argumentTypePlusOne = ArgumentType.TYPES_BY_STRING.get(stringArgPlusOne);
				if(argumentType != null){ // matches an anticipated argument string
					// so does next value, must be an argument too, reevaluate w/ index incremented only once above
					if(argumentTypePlusOne instanceof ArgumentType){ 
						commandLineArguments.put(argumentType, new CommandLineArgument(argumentType, null));
						// intentionally increment past next argument (only one) and
						// evaluate in next iteration with its following argument
						// - in other words, argumentType will be argumentTypePlusOne in next iteration
						continue;
					}
					// ok 2nd argument wasn't a recongized argument type - go
					// ahead and see if it's a class, if not, it must be a
					// method - or bogus
					else if(stringArgPlusOne != null){
						guessAtMethodAndClass(commandLineArguments, stringArgPlusOne);
					}
					else{
						commandLineArguments.put(argumentType, new CommandLineArgument(argumentType, null));
					}
					index++;
					continue;
				}else if(stringArg != null){
					// no flag, but a string - likely class or class and method
					guessAtMethodAndClass(commandLineArguments, stringArg);
					// do not increment again, bump stringArgPlusOne into stringArg from prior increment
					continue;
				}else{
					warnIgnored(stringArgPlusOne);
				}
			}
		}
		return commandLineArguments;
	}
	
	private static void guessAtMethodAndClass(
			Map<ArgumentType, CommandLineArgument> commandLineArguments,
			String potentialClassOrMethod) {
		boolean hasMethod = commandLineArguments.containsKey(ArgumentType.METHOD_ARG);
		boolean hasClass = commandLineArguments.containsKey(ArgumentType.CLASS_ARG);
		try{
			if(potentialClassOrMethod != null){
				if(!hasClass){
					commandLineArguments.put(ArgumentType.CLASS_ARG, 
						new CommandLineArgument(ArgumentType.CLASS_ARG, Class.forName(potentialClassOrMethod).getName()));
				}else if(!hasMethod){
					commandLineArguments.put(ArgumentType.METHOD_ARG, 
							new CommandLineArgument(ArgumentType.METHOD_ARG, potentialClassOrMethod));
				}
			}
		}catch(ClassNotFoundException cnfe2){
			if(!hasMethod){
				commandLineArguments.put(ArgumentType.METHOD_ARG, 
					new CommandLineArgument(ArgumentType.METHOD_ARG, potentialClassOrMethod));
			}else{
				throw new IllegalArgumentException(potentialClassOrMethod
						+ " does not match an expected argument, nor value.");
			}
		}
	}
	
	private static void warnIgnored(String a1) {
		System.out.println(a1+" was ignored");
	}
}

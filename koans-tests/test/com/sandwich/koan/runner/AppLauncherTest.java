package com.sandwich.koan.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.ArgumentType;

public class AppLauncherTest {

	@Test
	public void testNecessityOfAddingRunKoansCommandLineArgument_addsIfNoArgsPresent(){ //default target
		Map<ArgumentType, CommandLineArgument> args = new CommandLineArgumentBuilder();
		assertArgsContains(true, args, ArgumentType.RUN_KOANS);
	}

	@Test
	public void testNecessityOfAddingRunKoansCommandLineArgument_ifClassArgIsPresent(){
		Map<ArgumentType, CommandLineArgument> args = new CommandLineArgumentBuilder(Object.class.getName());
		assertArgsContains(true, args, ArgumentType.RUN_KOANS, ArgumentType.CLASS_ARG);
	}
	
	@Test
	public void testNecessityOfAddingRunKoansCommandLineArgument_doesntIfClassArgIsntPresent(){
		List<ArgumentType> types = new ArrayList<ArgumentType>(Arrays.asList(ArgumentType.values()));
		assertTrue(types.remove(ArgumentType.CLASS_ARG));
		assertTrue(types.remove(ArgumentType.DEBUG));
		assertTrue(types.remove(ArgumentType.RUN_KOANS));
		for(ArgumentType type : types){
			Map<ArgumentType, CommandLineArgument> args = new CommandLineArgumentBuilder(type.args().iterator().next());
			assertArgsContains(false, args, ArgumentType.RUN_KOANS);
			assertArgsContains(true, args, type);
		}
	}
	
	private static void assertArgsContains(
			boolean shouldContain, Map<ArgumentType, CommandLineArgument> args, ArgumentType...types) {
		if(shouldContain){
			assertEquals("expected arguments of a certain length, but found those built were of a differing size",
				types.length, args.size());
		}
		for(ArgumentType type : types){
			assertEquals("the arguments built should"
					+ (shouldContain ? "" : "n't")
					+ " contain the type: "+type, shouldContain, args.containsKey(type));
		}
	}
}

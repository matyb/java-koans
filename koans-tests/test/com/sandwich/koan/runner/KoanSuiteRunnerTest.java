package com.sandwich.koan.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.constant.ArgumentType;

public class KoanSuiteRunnerTest {

	@Test
	public void testNecessityOfAddingRunKoansCommandLineArgument_addsIfNoArgsPresent(){ //default target
		Map<ArgumentType, CommandLineArgument> args = new HashMap<ArgumentType, CommandLineArgument>();
		new KoanSuiteRunner().applyAssumedStartupBehaviors(args);
		assertArgsContains(true, args, ArgumentType.RUN_KOANS);
	}

	@Test
	public void testNecessityOfAddingRunKoansCommandLineArgument_ifClassArgIsPresent(){ //default target
		Map<ArgumentType, CommandLineArgument> args = new HashMap<ArgumentType, CommandLineArgument>();
		args.put(ArgumentType.CLASS_ARG, new CommandLineArgument(ArgumentType.CLASS_ARG, Object.class.getName()));
		new KoanSuiteRunner().applyAssumedStartupBehaviors(args);
		assertArgsContains(true, args, ArgumentType.RUN_KOANS, ArgumentType.CLASS_ARG);
	}
	
	@Test
	public void testNecessityOfAddingRunKoansCommandLineArgument_doesntIfClassArgIsntPresent(){
		List<ArgumentType> types = new ArrayList<ArgumentType>(Arrays.asList(ArgumentType.values()));
		assertTrue(types.remove(ArgumentType.CLASS_ARG));
		assertTrue(types.remove(ArgumentType.RUN_KOANS));
		for(ArgumentType type : types){
			Map<ArgumentType, CommandLineArgument> args = new HashMap<ArgumentType, CommandLineArgument>();
			args.put(type, new CommandLineArgument(type, null));
			new KoanSuiteRunner().applyAssumedStartupBehaviors(args);
			assertArgsContains(false, args, ArgumentType.RUN_KOANS);
			assertArgsContains(true, args, type);
		}
	}
	
	@Test
	public void testRunSortsAndInvokesByComparableImplInArgumentType() throws Exception {
		// test depends on this - this is to ensure rest of test is true
		{
			assertEquals(-1, ArgumentType.TEST.compareTo(ArgumentType.CLASS_ARG));
		}
		Map<ArgumentType, CommandLineArgument> args = new LinkedHashMap<ArgumentType, CommandLineArgument>();
		final boolean[] called = {false, false, false};
		args.put(ArgumentType.TEST, new CommandLineArgument(ArgumentType.TEST, null){
			@Override public void run(){
				assertFalse(called[0]);
				assertFalse(called[1]);
				assertFalse(called[2]);
				called[0] = true;
			}
		});
		args.put(ArgumentType.CLASS_ARG, new CommandLineArgument(ArgumentType.CLASS_ARG, null){
			@Override public void run(){
				assertTrue(called[0]);
				assertFalse(called[1]);
				assertFalse(called[2]);
				called[1] = true;
			}
		});
		args.put(ArgumentType.RUN_KOANS, new CommandLineArgument(ArgumentType.RUN_KOANS, null){
			@Override public void run(){
				assertTrue(called[0]);
				assertTrue(called[1]);
				assertFalse(called[2]);
				called[2] = true;
			}
		});
		new KoanSuiteRunner(args).run();
		assertTrue(called[0]);
		assertTrue(called[1]);
		assertTrue(called[2]);
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

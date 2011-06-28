package com.sandwich.koan.cmdline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.util.SimpleEntry;


public class CommandLineArgumentBuilderTest {

	@Test
	public void testNoArguments() throws Exception {
		assertEquals(ArgumentType.RUN_KOANS, new CommandLineArgumentBuilder().entrySet().iterator().next().getKey());
	}
	
	@Test
	public void testUnanticipatedArgument_yieldsMethodArg_constructedImplicitly() throws Exception {
		String value = 
			"if string isn't a known command line arg (ArgumentType) or class - assume its a method";
		Entry<ArgumentType, CommandLineArgument> anticipatedResult = 
			new SimpleEntry<ArgumentType, CommandLineArgument>(ArgumentType.METHOD_ARG, 
					new CommandLineArgument(ArgumentType.METHOD_ARG, value));
		Map<ArgumentType, CommandLineArgument> commandLineArgs = 
			new CommandLineArgumentBuilder(value);
		assertEquals(1, commandLineArgs.size());
		assertEquals(anticipatedResult, commandLineArgs.entrySet().iterator().next());
	}
	
	@Test
	public void testMethodArg_constructedExplicitly() throws Exception {
		String value = "someMethodName";
		Entry<ArgumentType, CommandLineArgument> anticipatedResult = 
			new SimpleEntry<ArgumentType, CommandLineArgument>(ArgumentType.METHOD_ARG, 
					new CommandLineArgument(ArgumentType.METHOD_ARG, value));
		Map<ArgumentType, CommandLineArgument> commandLineArgs = new CommandLineArgumentBuilder(
					ArgumentType.METHOD_ARG.args().iterator().next(),
					value);
		assertEquals(1, commandLineArgs.size());
		assertEquals(anticipatedResult, commandLineArgs.entrySet().iterator().next());
	}
	
	@Test
	public void testClassArg_constructedImplicitly() throws Exception {
		String value = Object.class.getName();
		Map<ArgumentType, CommandLineArgument> commandLineArgs = new CommandLineArgumentBuilder(value);
		assertEquals(2, commandLineArgs.size());
		assertTrue(commandLineArgs.containsKey(ArgumentType.CLASS_ARG));
		assertTrue(commandLineArgs.containsKey(ArgumentType.RUN_KOANS));
	}
	
	@Test
	public void testClassArg_constructedExplicitly() throws Exception {
		String value = Object.class.getName();
		Map<ArgumentType, CommandLineArgument> commandLineArgs = new CommandLineArgumentBuilder(
					ArgumentType.CLASS_ARG.args().iterator().next(), value);
		assertEquals(2, commandLineArgs.size());
		assertTrue(commandLineArgs.containsKey(ArgumentType.CLASS_ARG));
		assertTrue(commandLineArgs.containsKey(ArgumentType.RUN_KOANS));
	}
	
	@Test
	public void testMultipleMethodOrUnknownArgs_throwsException() throws Exception {
		String value = "someMethodName";
		try{
			new CommandLineArgumentBuilder(ArgumentType.METHOD_ARG.args().iterator().next(), value, value);
			fail();
		}catch(IllegalArgumentException x){
			
		}
	}
	
}

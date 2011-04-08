package com.sandwich.koan.cmdline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.util.SimpleEntry;


public class CommandLineArgumentBuilderTest {

	@Test
	public void testNoArguments() throws Exception {
		assertEquals(Collections.EMPTY_MAP, new CommandLineArgumentBuilder().build());
	}
	
	@Test
	public void testUnanticipatedArgument_yieldsMethodArg_constructedImplicitly() throws Exception {
		String value = 
			"if string isn't a known command line arg (ArgumentType) or class - assume its a method";
		Entry<ArgumentType, CommandLineArgument> anticipatedResult = 
			new SimpleEntry<ArgumentType, CommandLineArgument>(ArgumentType.METHOD_ARG, 
					new CommandLineArgument(ArgumentType.METHOD_ARG, value));
		Map<ArgumentType, CommandLineArgument> commandLineArgs = 
			new CommandLineArgumentBuilder(value).build();
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
					value).build();
		assertEquals(1, commandLineArgs.size());
		assertEquals(anticipatedResult, commandLineArgs.entrySet().iterator().next());
	}
	
	@Test
	public void testClassArg_constructedImplicitly() throws Exception {
		String value = Object.class.getName();
		Entry<ArgumentType, CommandLineArgument> anticipatedResult = 
			new SimpleEntry<ArgumentType, CommandLineArgument>(ArgumentType.CLASS_ARG, 
					new CommandLineArgument(ArgumentType.CLASS_ARG, value));
		Map<ArgumentType, CommandLineArgument> commandLineArgs = new CommandLineArgumentBuilder(
					value).build();
		assertEquals(1, commandLineArgs.size());
		assertEquals(anticipatedResult, commandLineArgs.entrySet().iterator().next());
	}
	
	@Test
	public void testClassArg_constructedExplicitly() throws Exception {
		String value = Object.class.getName();
		Entry<ArgumentType, CommandLineArgument> anticipatedResult = 
			new SimpleEntry<ArgumentType, CommandLineArgument>(ArgumentType.CLASS_ARG, 
					new CommandLineArgument(ArgumentType.CLASS_ARG, value));
		Map<ArgumentType, CommandLineArgument> commandLineArgs = new CommandLineArgumentBuilder(
					ArgumentType.CLASS_ARG.args().iterator().next(),
					value).build();
		assertEquals(1, commandLineArgs.size());
		assertEquals(anticipatedResult, commandLineArgs.entrySet().iterator().next());
	}
	
	@Test
	public void testMultipleMethodOrUnknownArgs_throwsException() throws Exception {
		String value = "someMethodName";
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder(
				ArgumentType.METHOD_ARG.args().iterator().next(),
				value,
				value);
		try{
			builder.build();
			fail();
		}catch(IllegalArgumentException x){
			
		}
	}
	
}

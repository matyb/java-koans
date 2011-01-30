package com.sandwich.koan.runner;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import com.sandwich.koan.TestUtils;
import com.sandwich.koan.TestUtils.ArgRunner;
import com.sandwich.koan.runner.PathToEnlightment;

public abstract class CommandLineTestCase {

	private PrintStream console;
	private ByteArrayOutputStream bytes;

	@Before
	public void setUp() {
		bytes = new ByteArrayOutputStream();
		console = System.out;
		PathToEnlightment.koans = PathToEnlightment.createKoansList();
		System.setOut(new PrintStream(bytes));
	}

	@After
	public void tearDown() {
		PathToEnlightment.koans = PathToEnlightment.createKoansList();
		System.setOut(console);
	}
	
	protected List<Class<?>> stubAllKoans(List<Class<?>> koans){
		List<Class<?>> oldKoans = PathToEnlightment.getPathToEnlightment();
		PathToEnlightment.koans = koans;
		return oldKoans;
	}
	
	protected List<Class<?>> stubAllKoans(Class<?>... koans){
		return stubAllKoans(Arrays.asList(koans));
	}
	
	protected void clearSysout(){
		bytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bytes));
	}
	
	protected void assertSystemOutEquals(String expectation){
		expectation = expectation == null ? "" : expectation;
		if(!expectation.equals(bytes.toString())){
			throw new AssertionFailedException(expectation, bytes.toString());
		}
	}
	
	protected void assertSystemOutContains(String expectation){
		assertSystemOutContains(true, expectation);
	}
	
	protected void assertSystemOutDoesntContain(String expectation){
		assertSystemOutContains(false, expectation);
	}
	
	private void assertSystemOutContains(boolean assertContains, String expectation) {
		String consoleOutput = bytes.toString();
		boolean containsTheSubstring = consoleOutput.contains(expectation);
		if(assertContains && !containsTheSubstring || !assertContains && containsTheSubstring){
			throw new AssertionFailedException(new StringBuilder(
					"<").append(
					expectation).append(
					"> ").append(
					(assertContains ? "wasn't" : "was")).append(
					" found in: " ).append(
					"<").append(
					consoleOutput).append(
					">").toString());
		}
	}

	protected void assertSystemOutLineEquals(final int lineNumber, final String lineText){
		assertSystemOutLineEquals(lineNumber, lineText, false);
	}
	
	protected void assertSystemOutLineEquals(final int lineNumber, final String lineText,
			final boolean trimLinesString) {
		final int[] onLine = new int[]{0};
		final boolean[] found = new boolean[]{false};
		TestUtils.forEachLine(bytes.toString(), new ArgRunner<String>(){
			public void run(String s){
				if(onLine[0] == lineNumber){
					if(trimLinesString){
						s = s.trim();
					}
					assertEquals(lineText, s);
					found[0] = true;
				}
				onLine[0]++;
			}
		});
		if(!found[0]){
			throw new AssertionFailedException(lineText+" was expected, but not found in: "+bytes.toString());
		}
	}

	static class AssertionFailedException extends AssertionError {
		private static final long serialVersionUID = -752171658596389715L;
		AssertionFailedException(Object o0, Object o1){
			super("expected: <"+o0+"> but found: <"+o1+">");
		}
		AssertionFailedException(Object o0){
			super(o0);
		}
	}
	
}

package com.sandwich.koan.path;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.TestUtils;
import com.sandwich.koan.TestUtils.ArgRunner;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.koan.path.PathToEnlightenment.Path;

public abstract class CommandLineTestCase {

	private PrintStream console;
	private ByteArrayOutputStream bytes;

	@Before
	public void setUp() {
		bytes = new ByteArrayOutputStream();
		console = System.out;
		PathToEnlightenment.theWay = PathToEnlightenment.createPath();
		System.setOut(new PrintStream(bytes));
	}

	@After
	public void tearDown() {
		PathToEnlightenment.theWay = PathToEnlightenment.createPath();
		System.setOut(console);
	}
	
	protected Path stubAllKoans(String packageName, List<?> path){
		Path oldKoans = PathToEnlightenment.getPathToEnlightment();
		Map<Object, List<KoanMethod>> tempSuitesAndMethods = new LinkedHashMap<Object, List<KoanMethod>>();
		for(Object suite : path){
			try {
				if(suite instanceof Class<?>){
					PathToEnlightenment.stagePathToEnlightenment((Class<?>)suite);
				}else{
					PathToEnlightenment.stagePathToEnlightenment(suite);
				}
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			for(Entry<String, Map<Object, List<KoanMethod>>> e : PathToEnlightenment.theWay){
				tempSuitesAndMethods.putAll(e.getValue());
			}
		}
		Map<String, Map<Object, List<KoanMethod>>> stubbedPath = new HashMap<String, Map<Object,List<KoanMethod>>>();
		stubbedPath.put(packageName, tempSuitesAndMethods);
		PathToEnlightenment.theWay = new Path(stubbedPath);
		return oldKoans;
	}
	
	public Path stubAllKoans(List<?> path){
		return stubAllKoans("Test", path);
	}
	
	public void clearSysout(){
		bytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bytes));
	}
	
	public void assertSystemOutEquals(String expectation){
		expectation = expectation == null ? "" : expectation;
		if(!expectation.equals(bytes.toString())){
			throw new AssertionFailedException(expectation, bytes.toString());
		}
	}
	
	public void assertSystemOutContains(String expectation){
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

	public void assertSystemOutLineEquals(final int lineNumber, final String lineText){
		assertSystemOutLineEquals(lineNumber, lineText, false);
	}
	
	public void assertSystemOutLineEquals(final int lineNumber, final String lineText,
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

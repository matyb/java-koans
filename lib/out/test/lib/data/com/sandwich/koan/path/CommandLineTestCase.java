package com.sandwich.koan.path;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanIncompleteException;
import com.sandwich.koan.TestUtils;
import com.sandwich.koan.TestUtils.ArgRunner;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.path.PathToEnlightenment.Path;
import com.sandwich.koan.path.xmltransformation.FakeXmlToPathTransformer;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.runner.RunKoans;
import com.sandwich.util.io.DynamicClassLoader;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.directories.Production;
import com.sandwich.util.io.directories.UnitTest;

public abstract class CommandLineTestCase {

	private PrintStream console;
	private ByteArrayOutputStream bytes;
	
	@Before
	public void setUp() {
		DirectoryManager.setDirectorySet(new UnitTest());
		bytes = new ByteArrayOutputStream();
		console = System.out;
		TestUtils.setValue("behavior", new RunKoans(), ArgumentType.RUN_KOANS);
		PathToEnlightenment.xmlToPathTransformer = new FakeXmlToPathTransformer();
		PathToEnlightenment.theWay = PathToEnlightenment.createPath();
		System.setOut(new PrintStream(bytes));
	}

	@After
	public void tearDown() {
		DirectoryManager.setDirectorySet(new Production());
		setRealPath();
		System.setOut(console);
	}
	
	protected void setRealPath(){
		PathToEnlightenment.xmlToPathTransformer = null;
		PathToEnlightenment.theWay = PathToEnlightenment.createPath();
	}
	
	protected Path stubAllKoans(String packageName, List<String> path){
		Path oldKoans = PathToEnlightenment.getPathToEnlightenment();
		Map<String, Map<String, KoanElementAttributes>> tempSuitesAndMethods = 
			new LinkedHashMap<String, Map<String, KoanElementAttributes>>();
		DynamicClassLoader loader = new DynamicClassLoader();
		for(String suite : path){
			Map<String, KoanElementAttributes> methodsByName = new LinkedHashMap<String, KoanElementAttributes>();
			for(Method m : loader.loadClass(suite).getMethods()){
				if(m.getAnnotation(Koan.class) != null){
					methodsByName.put(m.getName(), new KoanElementAttributes("", m.getName(), "", m.getDeclaringClass().getName()));
				}
			}
			tempSuitesAndMethods.put(suite, methodsByName);
		}
		Map<String, Map<String, Map<String, KoanElementAttributes>>> stubbedPath = 
			new LinkedHashMap<String, Map<String, Map<String, KoanElementAttributes>>>();
		stubbedPath.put(packageName, tempSuitesAndMethods);
		PathToEnlightenment.theWay = new Path(null,stubbedPath);
		return oldKoans;
	}
	
	public Path stubAllKoans(List<?> path){
		List<String> classes = new ArrayList<String>();
		for(Object o : path){
			String className;
			if(o instanceof Class<?>){
				className = ((Class<?>)o).getName();
			}else{
				className = o.getClass().getName();
			}
			classes.add(className);
		}
		return stubAllKoans("Test", classes);
	}
	
	public void clearSysout(){
		bytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bytes));
	}
	
	public void assertSystemOutEquals(String expectation){
		expectation = expectation == null ? "" : expectation;
		if(!expectation.equals(bytes.toString())){
			throw new KoanIncompleteException("expected: <"+expectation+"> but found: <"+bytes.toString()+">");
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
			throw new KoanIncompleteException(new StringBuilder(
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
			throw new KoanIncompleteException(lineText+" was expected, but not found in: "+bytes.toString());
		}
	}
}

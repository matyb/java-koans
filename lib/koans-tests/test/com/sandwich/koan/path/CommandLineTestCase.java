package com.sandwich.koan.path;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanClassLoader;
import com.sandwich.koan.KoanIncompleteException;
import com.sandwich.koan.TestUtils;
import com.sandwich.koan.TestUtils.ArgRunner;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.path.PathToEnlightenment.Path;
import com.sandwich.koan.path.xmltransformation.FakeXmlToPathTransformer;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.runner.RunKoans;
import com.sandwich.koan.ui.ConsolePresenter;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.koan.util.ApplicationUtils.SuitePresenterFactory;
import com.sandwich.util.io.DynamicClassLoader;
import com.sandwich.util.io.KoanSuiteCompilationListener;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.directories.Production;
import com.sandwich.util.io.directories.UnitTest;

public abstract class CommandLineTestCase {

	private PrintStream out;
	private PrintStream err;
	private ByteArrayOutputStream outBytes;
	private ByteArrayOutputStream errBytes;
	
	@Before
	public void setUp() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		DirectoryManager.setDirectorySet(new UnitTest());
		KoanClassLoader.setInstance(KoanClassLoader.createInstance());
		out = System.out;
		err = System.err;
		TestUtils.setValue("behavior", new RunKoans(), ArgumentType.RUN_KOANS);
		PathToEnlightenment.xmlToPathTransformer = new FakeXmlToPathTransformer();
		PathToEnlightenment.theWay = PathToEnlightenment.createPath();
		clearSystemOutputs();
		stubPresenter(new ConsolePresenter());
	}

	@After
	public void tearDown() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		DirectoryManager.setDirectorySet(new Production());
		setRealPath();
		System.setOut(out);
		System.setErr(err);
		KoanClassLoader.setInstance(KoanClassLoader.createInstance());
	}
	
	protected void setRealPath(){
		PathToEnlightenment.xmlToPathTransformer = null;
		PathToEnlightenment.theWay = PathToEnlightenment.createPath();
	}
	
	protected void stubPresenter(final SuitePresenter presenter)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = ApplicationUtils.class.getDeclaredField("suitePresenterFactory");
		boolean isAccessible = f.isAccessible();
		try {
			f.setAccessible(true);
			f.set(ApplicationUtils.class, new SuitePresenterFactory(){
				@Override public SuitePresenter create(){
					return presenter;
				}
			});
		} finally {
			f.setAccessible(isAccessible);
		}
	}
	
	protected Path stubAllKoans(String packageName, List<String> path){
		Path oldKoans = PathToEnlightenment.getPathToEnlightenment();
		Map<String, Map<String, KoanElementAttributes>> tempSuitesAndMethods = 
			new LinkedHashMap<String, Map<String, KoanElementAttributes>>();
		DynamicClassLoader loader = KoanClassLoader.getInstance();
		for(String suite : path){
			Map<String, KoanElementAttributes> methodsByName = new LinkedHashMap<String, KoanElementAttributes>();
			KoanSuiteCompilationListener listener = new KoanSuiteCompilationListener();
			for(Method m : loader.loadClass(suite, listener).getMethods()){
				if(m.getAnnotation(Koan.class) != null){
					methodsByName.put(m.getName(), new KoanElementAttributes(m.getName(), "", m.getDeclaringClass().getName()));
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
	
	public void clearSystemOutputs(){
		outBytes = new ByteArrayOutputStream();
		errBytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outBytes));
		System.setErr(new PrintStream(errBytes));
	}
	
	public void assertSystemOutEquals(String expectation){
		assertEquals(expectation, outBytes);
	}
	
	public void assertSystemErrEquals(String expectation){
		assertEquals(expectation, errBytes);
	}
	
	private void assertEquals(String expectation, ByteArrayOutputStream bytes){
		expectation = expectation == null ? "" : expectation;
		String bytesString = String.valueOf(bytes);
		if(!expectation.equals(bytesString)){
			throw new KoanIncompleteException("expected: <"+expectation+"> but found: <"+bytesString+">");
		}
	}
	
	public void assertSystemOutContains(String expectation){
		assertContains(true, expectation, outBytes);
	}
	
	public void assertSystemOutDoesntContain(String expectation){
		assertContains(false, expectation, outBytes);
	}
	
	public void assertSystemErrContains(String expectation){
		assertContains(true, expectation, errBytes);
	}
	
	public void assertSystemErrDoesntContain(String expectation){
		assertContains(false, expectation, errBytes);
	}

	private void assertContains(boolean assertContains, String expectation, ByteArrayOutputStream bytes) {
		String consoleOutput = String.valueOf(bytes.toString());
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
		assertLineEquals(lineNumber, lineText, false, outBytes);
	}
	
	public void assertSystemErrLineEquals(final int lineNumber, final String lineText){
		assertLineEquals(lineNumber, lineText, false, errBytes);
	}
	
	public void assertLineEquals(final int lineNumber, final String lineText, final boolean trimLinesString, ByteArrayOutputStream bytes) {
		final int[] onLine = new int[]{0};
		final boolean[] found = new boolean[]{false};
		String bytesString = String.valueOf(bytes);
		TestUtils.forEachLine(bytesString, new ArgRunner<String>(){
			public void run(String s){
				if(onLine[0] == lineNumber){
					if(trimLinesString){
						s = s.trim();
					}
					Assert.assertEquals(lineText, s);
					found[0] = true;
				}
				onLine[0]++;
			}
		});
		if(!found[0]){
			throw new KoanIncompleteException(lineText+" was expected, but not found in: "+bytesString);
		}
	}
}

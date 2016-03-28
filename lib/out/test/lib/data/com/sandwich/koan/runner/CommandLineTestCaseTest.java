package com.sandwich.koan.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.koan.path.PathToEnlightenment.Path;

public class CommandLineTestCaseTest {

	CommandLineTestCase testCase;
	
	@Before
	public void setUp(){
		testCase = new CommandLineTestCase(){};
	}
	
	@After
	public void tearDown(){
		testCase.tearDown(); // really important - will remove regular System.out if commented!
		testCase = null;
	}
	
	@Test
	public void testThatConsoleIsInitializedBySetUp() throws Exception {
		PrintStream originalOut = System.out;
		testCase.setUp();
		assertNotSame(originalOut, System.out);
	}
	
	@Test
	public void testThatConsoleIsCleanAfterSetUp() throws Exception {
		testCase.setUp();
		testCase.assertSystemOutLineEquals(0, "");
	}
	
	@Test
	public void testThatConsoleIsAttachedToSystem() throws Exception {
		testCase.setUp();
		System.out.print("hello \n world!");
		testCase.assertSystemOutLineEquals(0, "hello ");
		testCase.assertSystemOutLineEquals(1, " world!");
	}
	
	@Test
	public void testThatAssertSystemOutLineEquals_withTrimStringArg() throws Exception {
		testCase.setUp();
		System.out.println(" hello \n world! ");
		testCase.assertSystemOutLineEquals(0, "hello", true);
		testCase.assertSystemOutLineEquals(1, "world!", true);
	}
	
	@Test
	public void testThatTearDownDetachesDummiedConsoleFromSystem(){
		PrintStream originalConsole = System.out;
		testCase.setUp();
		PrintStream fakeConsole = System.out;
		assertNotSame(fakeConsole, originalConsole);
		testCase.tearDown();
		assertSame(originalConsole, System.out);
	}
	
	@Test
	public void testThatStubAllKoansStubsAllKoansReference() throws Exception {
		Path oldKoans = PathToEnlightenment.getPathToEnlightenment();
		List<Object> newKoans = Collections.emptyList();
		testCase.stubAllKoans(newKoans);
		assertNotSame(oldKoans, PathToEnlightenment.getPathToEnlightenment());
		// number of suites
		assertEquals(0, PathToEnlightenment.getPathToEnlightenment()
				.iterator().next().getValue().size()); 
	}
	
	@Test
	public void testTestCaseRestoresAllKoansReference() throws Exception {
		Path oldKoans = PathToEnlightenment.getPathToEnlightenment();
		List<Object> newKoans = Collections.emptyList();
		testCase.stubAllKoans(newKoans);
		assertNotSame(oldKoans, PathToEnlightenment.getPathToEnlightenment());
		testCase.tearDown();
		// creates all new instance
		assertNotSame(oldKoans, PathToEnlightenment.getPathToEnlightenment());
		assertEquals(oldKoans, PathToEnlightenment.getPathToEnlightenment());
	}
	
	@Test
	public void testClearSysout() throws Exception {
		testCase.setUp();
		System.out.print("!");
		testCase.assertSystemOutLineEquals(0, "!");
		testCase.clearSysout();
		testCase.assertSystemOutLineEquals(0, "");
	}
	
	@Test
	public void testSystemOutEquals_freshStart(){
		testCase.setUp();
		testCase.assertSystemOutEquals("");
	}
	
	@Test
	public void testSystemOutEquals_aString() throws Exception {
		String helloWorld = "Hello World!";
		testCase.setUp();
		testCase.assertSystemOutEquals("");
		System.out.print(helloWorld);
		testCase.assertSystemOutEquals(helloWorld);
	}
	
	@Test
	public void testSystemOutContains_happyPath() throws Exception {
		testCase.setUp();
		String zeroThruOne = "01";
		System.out.print(zeroThruOne);
		for(char c : zeroThruOne.toCharArray()){
			testCase.assertSystemOutContains(String.valueOf(c));
		}
	}
	
	@Test
	public void testSystemOutContains_failurePath() throws Exception {
		testCase.setUp();
		String zeroThruOne = "01";
		System.out.print(zeroThruOne);
		try{
			testCase.assertSystemOutContains(String.valueOf("a"));
			fail();
		}catch(AssertionError ex){
			assertEquals("<a> wasn't found in: <01>", ex.getMessage());
		}
	}
}

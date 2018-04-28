package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.EXPECTATION_LEFT_ARG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.koan.suite.BlowUpOnLineEleven;
import com.sandwich.koan.suite.BlowUpOnLineTen;
import com.sandwich.koan.suite.OnePassingKoan;
import com.sandwich.koan.suite.TwoFailingKoans;
import com.sandwich.koan.suite.WrongExpectationOrderKoan;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.util.Strings;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.directories.ProductionDirectories;

/**
 * Anything that absolutely has to happen before bundling client jar - to be sure:
 * - all koans fail by default
 * - necessary aspects of app presentation are preserved
 * - progression through koans (the sequence of koans) is consistent
 */
public class AppReadinessForDeploymentTest extends CommandLineTestCase {
	
	@Before 
	public void setUp() throws Exception  {
		super.setUp();
		resetHandlers();
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		resetHandlers();
	}
	
	private void resetHandlers(){
		for(Handler handler : Logger.getLogger(CommandLineArgumentRunner.class.getSimpleName()).getHandlers()){
			Logger.getLogger(CommandLineArgumentRunner.class.getSimpleName()).removeHandler(handler);
		}
	}
	
	@Test
	public void testMainMethodWithClassNameArg_qualifiedWithPkgName() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(Strings.getMessage("passing_suites")+": "+OnePassingKoan.class.getSimpleName());
	}

	@Test
	public void testMainMethodWithClassNameArg_classSimpleName() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(Strings.getMessage("passing_suites")+": "+OnePassingKoan.class.getSimpleName());
	}
	
	@Test
	public void testMainMethodWithClassNameArg_classNameAndMethod() throws Throwable {
		stubAllKoans(Arrays.asList(new TwoFailingKoans()));
		String failingKoanMethodName = TwoFailingKoans.class.getMethod("koanTwo").getName();
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(failingKoanMethodName);
		assertSystemOutContains("0/2");
	}
	
	@Test
	public void testGetKoans() throws Exception {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		Map<String, Map<String, KoanElementAttributes>> koans = PathToEnlightenment.getPathToEnlightenment().iterator().next().getValue();
		assertEquals(1, koans.size());
		Entry<String, Map<String, KoanElementAttributes>> entry = koans.entrySet().iterator().next();
		assertEquals(OnePassingKoan.class.getName(), entry.getKey());
		assertEquals(	OnePassingKoan.class.getDeclaredMethod("koan").toString(), 
						KoanMethod.getInstance(entry.getValue().get("koan")).getMethod().toString());
	}
	
	@Test
	public void testKoanSuiteRunner_firstKoanFail() throws Exception {
		final KoanSuiteResult[] result = new KoanSuiteResult[]{null};
		stubPresenter(new SuitePresenter(){
				public void displayResult(KoanSuiteResult actualAppResult) {
					// don't display, capture them so we can analyze and ensure first failure is reported
					result[0] = actualAppResult;
				}
				public void displayError(String error) {fail();}
				public void displayMessage(String msg) {fail();}
				public void clearMessages() {}
			});
		doAsIfInProd(new Runnable(){
			public void run(){
				new RunKoans(PathToEnlightenment.getPathToEnlightenment()).run(new String[0]);
			}
		});
		String firstSuiteClassRan = PathToEnlightenment.getPathToEnlightenment()
				.iterator().next().getValue().entrySet().iterator().next().getKey();
		assertEquals(result[0].getFailingCase(), firstSuiteClassRan.substring(firstSuiteClassRan.lastIndexOf(".") + 1));
	}

	@Test
	public void testKoanSuiteRunner_allKoansFail() throws Exception {
		setRealPath();
		final KoanSuiteResult[] result = new KoanSuiteResult[]{null};
		stubPresenter(new SuitePresenter(){
			public void displayResult(KoanSuiteResult actualAppResult) {
				// don't display, capture them so we can analyze and ensure first failure is reported
				result[0] = actualAppResult;
			}
			public void displayError(String error) {
				fail();
			}
			public void displayMessage(String msg) {fail();}
			public void clearMessages() {}
		});
		doAsIfInProd(new Runnable(){
			public void run(){
				new RunKoans(PathToEnlightenment.getPathToEnlightenment()).run(new String[0]);
			}
		});
		String message = "Not all koans need solving! Each should ship in a failing state.";
		assertEquals(message, 0, result[0].getNumberPassing());
		// make sure test was actually useful (ie something actually failed)
		assertNotNull(result[0].getFailingCase());
	}
	
	private void doAsIfInProd(Runnable runnable) {
		DirectoryManager.setDirectorySet(new ProductionDirectories());
		resetClassLoader();
		setRealPath();
		runnable.run();
	}

	@Test
	public void testLineExceptionIsThrownAtIsHintedAt() throws Exception {
		stubAllKoans(Arrays.asList(new BlowUpOnLineTen()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains("Line 10");
		assertSystemOutDoesntContain("Line 11");
	}
	
	@Test
	public void testLineExceptionIsThrownAtIsHintedAtEvenIfThrownFromSuperClass() throws Exception {
		stubAllKoans(Arrays.asList(new BlowUpOnLineEleven()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains("Line 11");
		assertSystemOutDoesntContain("Line 10");
	}
	
	@Test
	public void testWarningFromPlacingExpectationOnWrongSide() throws Throwable {
		final String[] message = new String[1];
		stubAllKoans(Arrays.asList(new WrongExpectationOrderKoan()));
		Logger.getLogger(CommandLineArgumentRunner.class.getSimpleName()).addHandler(
				new Handler() {
					@Override
					public void close() throws SecurityException {
					}

					@Override
					public void flush() {
					}

					@Override
					public void publish(LogRecord arg0) {
						message[0] = arg0.getMessage();
					}
				});
		new CommandLineArgumentRunner(new CommandLineArgumentBuilder()).run();
		assertEquals(
				new StringBuilder(
						WrongExpectationOrderKoan.class.getSimpleName())
						.append(".expectationOnLeft ")
						.append(EXPECTATION_LEFT_ARG).toString(), message[0]);
	}

	@Test
	public void testNoWarningFromPlacingExpectationOnRightSide()
			throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		Logger.getLogger(CommandLineArgumentRunner.class.getSimpleName()).addHandler(
				new Handler() {
					@Override
					public void close() throws SecurityException {
					}

					@Override
					public void flush() {
					}

					@Override
					public void publish(LogRecord arg0) {
						fail("No logging necessary when koan passes, otherwise - logging is new, adjust accordingly.\n"+arg0.getMessage());
					}
				});
		new CommandLineArgumentRunner().run();
	}
}

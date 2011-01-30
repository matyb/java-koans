package com.sandwich.koan.runner;

import static com.sandwich.koan.KoanConstants.__;
import static com.sandwich.koan.KoanConstants.EXPECTATION_LEFT_ARG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanConstants;
import com.sandwich.koan.KoansResult;
import com.sandwich.koan.runner.ui.SuitePresenter;
import com.sandwich.koan.suite.OneFailingKoan;
import com.sandwich.koan.suite.OneFailingKoanDifferentName;
import com.sandwich.koan.suite.OnePassingKoan;


public class KoanSuiteRunnerTest extends CommandLineTestCase {
	
	@Test
	public void testMainMethodWithClassNameArg_qualifiedWithPkgName() throws Throwable {
		KoanSuiteRunner.main(OnePassingKoan.class.getName());
		assertSystemOutContains(KoanConstants.PASSING_SUITES+" "+OnePassingKoan.class.getSimpleName());
	}
	
	@Test
	public void testMainMethodWithClassNameArg_classSimpleName() throws Throwable {
		KoanSuiteRunner.main(OnePassingKoan.class.getSimpleName());
		assertSystemOutContains(KoanConstants.PASSING_SUITES+" "+OnePassingKoan.class.getSimpleName());
	}
	
	@Test
	public void testMainMethodWithClassNameArg_classNameAndMethod() throws Throwable {
		String failingKoanMethodName = TwoFailingKoans.class.getDeclaredMethod("koanTwo").getName();
		KoanSuiteRunner.main(TwoFailingKoans.class.getName(), failingKoanMethodName);
		assertSystemOutContains(failingKoanMethodName);
		assertSystemOutDoesntContain(OneFailingKoan.class.getDeclaredMethods()[0].getName());
	}
	
	static class TwoFailingKoans extends OneFailingKoan {
		@Koan
		public void koanTwo(){assertEquals(true, false);}
	}
	
	@Test
	public void testThatKoanSuite_methodInvokationOrderingIsSensible() throws Exception {
		// TODO: written before EasyMock was imported, clean up
		final List<Integer> orderInvoked = new ArrayList<Integer>();
		new KoanSuiteRunner(){
			@Override Map<Object, List<Method>> getKoans(){
				orderInvoked.add(1);
				return null;
			}
			@Override KoansResult runKoans(Map<Object, List<Method>> koans){
				orderInvoked.add(2);
				return null;
			}
			@Override SuitePresenter getPresenter(){
				return new SuitePresenter() {
					@Override public void displayResult(KoansResult result) {
						orderInvoked.add(3);
					}
				};
			}
		}.run();
		assertEquals(orderInvoked, Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)));
	}
	
	@Test
	public void testGetKoans() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[]{OnePassingKoan.class}));
		Map<Object, List<Method>> koans = new KoanSuiteRunner().getKoans();
		assertEquals(1, koans.size());
		Entry<Object, List<Method>> entry = koans.entrySet().iterator().next();
		assertEquals(OnePassingKoan.class, entry.getKey().getClass());
		assertEquals(OnePassingKoan.class.getDeclaredMethod("koan"), entry.getValue().get(0));
	}
	
	@Test	/** Ensures that koans are ready for packaging & distribution */
	public void testKoanSuiteRunner_firstKoanFail() throws Exception {
		final KoansResult[] result = new KoansResult[]{null};
		new KoanSuiteRunner(){
			@Override public KoansResult runKoans(Map<Object, List<Method>> koans) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
				result[0] = super.runKoans(koans);
				return result[0];
			}
		}.run();
		assertEquals(result[0].getFailingCase(), PathToEnlightment.getPathToEnlightment().get(0));
	}
	
	@Test	/** Ensures that koans are ready for packaging & distribution */
	public void testKoanSuiteRunner_allKoansFail() throws Exception {
		final KoansResult[] result = new KoansResult[]{null};
		new KoanSuiteRunner(){
			@Override SuitePresenter getPresenter(){
				return new SuitePresenter() {
					@Override public void displayResult(KoansResult r) {
						result[0] = r;
					}
				};
			}
		}.run();
		String message = "Not all koans need solving! Each should ship in a failing state.";
		assertEquals(message, PathToEnlightment.getPathToEnlightment().get(0), 	result[0].getFailingCase());
		assertEquals(message, 0, 							result[0].getNumberPassing());
	}
	
	@Test
	public void testKoanDescriptionAppearsOnFailure() throws Throwable {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains("fake_description");
	}
	
	@Test
	public void testWarningFromPlacingExpecationOnWrongSide() throws Throwable {
		final String[] message = new String[1];
		stubAllKoans(WrongExpectationOrderKoan.class);
		Logger.getLogger(KoanSuiteRunner.class.getSimpleName()).addHandler(new Handler(){
			@Override public void close() throws SecurityException {}
			@Override public void flush() {}
			@Override public void publish(LogRecord arg0) {
				message[0] = arg0.getMessage();
			}
		});
		KoanSuiteRunner.main();
		assertEquals(new StringBuilder(
				WrongExpectationOrderKoan.class.getSimpleName()).append(
				".expectationOnLeft ").append(
				EXPECTATION_LEFT_ARG).toString()
				, message[0]);
	}
	
	@Test
	public void testNoWarningFromPlacingExpecationOnRightSide() throws Throwable {
		stubAllKoans(OnePassingKoan.class);
		Logger.getLogger(KoanSuiteRunner.class.getSimpleName()).addHandler(new Handler(){
			@Override public void close() throws SecurityException {}
			@Override public void flush() {}
			@Override public void publish(LogRecord arg0) {
				fail("No logging necessary when koan passes, otherwise - logging is new, adjust accordingly.");
			}
		});
		KoanSuiteRunner.main();
	}
	
	static class WrongExpectationOrderKoan {
		@Koan 
		public void expectationOnLeft(){
			assertEquals(__, false);
		}
	}
}

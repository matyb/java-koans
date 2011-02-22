package com.sandwich.koan.runner;

import static com.sandwich.koan.KoanConstants.EXPECTATION_LEFT_ARG;
import static com.sandwich.koan.KoanConstants.__;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.KoansResult;
import com.sandwich.koan.runner.ui.SuitePresenter;
import com.sandwich.koan.suite.BlowUpOnLineEleven;
import com.sandwich.koan.suite.BlowUpOnLineTen;
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
		KoanSuiteRunner.main(OnePassingKoan.class.getPackage().getName()
				+ KoanConstants.PERIOD + OnePassingKoan.class.getSimpleName());
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
			@Override KoansResult runKoans(){
				orderInvoked.add(1);
				return null;
			}
			@Override SuitePresenter getPresenter(){
				return new SuitePresenter() {
					@Override public void displayResult(KoansResult result) {
						orderInvoked.add(2);
					}
				};
			}
		}.run();
		assertEquals(orderInvoked, Arrays.asList(Integer.valueOf(1), Integer.valueOf(2)));
	}
	
	@Test @SuppressWarnings("unchecked")
	public void testGetKoans() throws Exception {
		stubAllKoans(Arrays.asList(OnePassingKoan.class));
		Map<Object, List<KoanMethod>> koans = new KoanSuiteRunner()
				.getPathToEnlightenment().iterator().next().getValue();
		assertEquals(1, koans.size());
		Entry<Object, List<KoanMethod>> entry = koans.entrySet().iterator().next();
		assertEquals(OnePassingKoan.class, entry.getKey().getClass());
		assertEquals(OnePassingKoan.class.getDeclaredMethod("koan"), entry.getValue().get(0).getMethod());
	}
	
	@Test	/** Ensures that koans are ready for packaging & distribution */
	public void testKoanSuiteRunner_firstKoanFail() throws Exception {
		final KoansResult[] result = new KoansResult[]{null};
		new KoanSuiteRunner(){
			@Override public KoansResult runKoans(){
				try {
					result[0] = super.runKoans();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return result[0];
			}
		}.run();
		assertEquals(result[0].getFailingCase(), PathToEnlightenment.getPathToEnlightment()
				.iterator().next().getValue().entrySet().iterator().next().getKey().getClass());
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
		assertEquals(message, 0,
				result[0].getNumberPassing());
	}
	
	@Test
	public void testKoanDescriptionAppearsOnFailure() throws Throwable {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains("fake_description");
	}
	
	@Test @SuppressWarnings("unchecked")
	public void testLineExceptionIsThrownAtIsHintedAt() throws Exception {
		stubAllKoans(Arrays.asList(BlowUpOnLineTen.class));
		new KoanSuiteRunner().run();
		assertSystemOutContains("Line 10");
		assertSystemOutDoesntContain("Line 11");
	}
	
	@Test @SuppressWarnings("unchecked")
	public void testLineExceptionIsThrownAtIsHintedAtEvenIfThrownFromSuperClass() throws Exception {
		stubAllKoans(Arrays.asList(BlowUpOnLineEleven.class));
		new KoanSuiteRunner().run();
		assertSystemOutContains("Line 11");
		assertSystemOutDoesntContain("Line 10");
	}
	
	@Test @SuppressWarnings("unchecked")
	public void testWarningFromPlacingExpecationOnWrongSide() throws Throwable {
		final String[] message = new String[1];
		stubAllKoans(Arrays.asList(WrongExpectationOrderKoan.class));
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
	
	@Test @SuppressWarnings("unchecked")
	public void testNoWarningFromPlacingExpecationOnRightSide() throws Throwable {
		stubAllKoans(Arrays.asList(OnePassingKoan.class));
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

package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.EXPECTATION_LEFT_ARG;
import static com.sandwich.koan.constant.KoanConstants.__;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.KoanSuiteResult;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.koan.suite.BlowUpOnLineEleven;
import com.sandwich.koan.suite.BlowUpOnLineTen;
import com.sandwich.koan.suite.OneFailingKoan;
import com.sandwich.koan.suite.OnePassingKoan;
import com.sandwich.koan.ui.SuitePresenter;

/**
 * Anything that absoutely has to happen before bundling client jar - to be
 * sure: - all koans fail by default - necessary aspects of app presentation are
 * preserved - progression through koans (the sequence of koans) is consistent
 */
public class AppReadinessForDeploymentTest extends CommandLineTestCase {

	@Test
	public void testMainMethodWithClassNameArg_qualifiedWithPkgName()
			throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		new KoanSuiteRunner().run();
		assertSystemOutContains(KoanConstants.PASSING_SUITES + " "
				+ OnePassingKoan.class.getSimpleName());
	}

	@Test
	public void testMainMethodWithClassNameArg_classSimpleName()
			throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		new KoanSuiteRunner().run();
		assertSystemOutContains(KoanConstants.PASSING_SUITES + " "
				+ OnePassingKoan.class.getSimpleName());
	}

	@Test
	public void testMainMethodWithClassNameArg_classNameAndMethod()
			throws Throwable {
		String failingKoanMethodName = TwoFailingKoans.class.getDeclaredMethod(
				"koanTwo").getName();
		stubAllKoans(Arrays.asList(new TwoFailingKoans()));
		new KoanSuiteRunner().run();
		assertSystemOutContains(failingKoanMethodName);
		assertSystemOutDoesntContain(OneFailingKoan.class.getDeclaredMethods()[0]
				.getName());
	}

	public static class TwoFailingKoans extends OneFailingKoan {
		@Koan
		public void koanTwo() {
			assertEquals(true, false);
		}
	}

	@Test
	public void testGetKoans() throws Exception {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		Map<Object, List<KoanMethod>> koans = PathToEnlightenment
				.getPathToEnlightment().iterator().next().getValue();
		assertEquals(1, koans.size());
		Entry<Object, List<KoanMethod>> entry = koans.entrySet().iterator()
				.next();
		assertEquals(OnePassingKoan.class, entry.getKey().getClass());
		assertEquals(OnePassingKoan.class.getDeclaredMethod("koan"), entry
				.getValue().get(0).getMethod());
	}

	@Test
	/** Ensures that koans are ready for packaging & distribution */
	public void testKoanSuiteRunner_firstKoanFail() throws Exception {
		final KoanSuiteResult[] result = new KoanSuiteResult[] { null };
		final SuitePresenter presenter = new SuitePresenter() {
			public void displayResult(KoanSuiteResult actualAppResult) {
				// don't display, capture them so we can analyze and ensure
				// first failure is reported
				result[0] = actualAppResult;
			}
		};
		new RunKoans(presenter, PathToEnlightenment.getPathToEnlightment())
				.run(null);
		assertEquals(result[0].getFailingCase(), PathToEnlightenment
				.getPathToEnlightment().iterator().next().getValue().entrySet()
				.iterator().next().getKey().getClass());
	}

	@Test
	/** Ensures that koans are ready for packaging & distribution */
	public void testKoanSuiteRunner_allKoansFail() throws Exception {
		final KoanSuiteResult[] result = new KoanSuiteResult[] { null };
		final SuitePresenter presenter = new SuitePresenter() {
			public void displayResult(KoanSuiteResult actualAppResult) {
				// don't display, capture them so we can analyze and ensure
				// first failure is reported
				result[0] = actualAppResult;
			}
		};
		new RunKoans(presenter, PathToEnlightenment.getPathToEnlightment())
				.run(null);
		String message = "Not all koans need solving! Each should ship in a failing state.";
		assertEquals(message, 0, result[0].getNumberPassing());
		// make sure test was actually useful (ie something actually failed)
		assertNotNull(result[0].getFailingCase());
	}

	@Test
	public void testLineExceptionIsThrownAtIsHintedAt() throws Exception {
		stubAllKoans(Arrays.asList(new BlowUpOnLineTen()));
		new KoanSuiteRunner(new CommandLineArgumentBuilder()).run();
		assertSystemOutContains("Line 10");
		assertSystemOutDoesntContain("Line 11");
	}

	@Test
	public void testLineExceptionIsThrownAtIsHintedAtEvenIfThrownFromSuperClass()
			throws Exception {
		stubAllKoans(Arrays.asList(new BlowUpOnLineEleven()));
		new KoanSuiteRunner(new CommandLineArgumentBuilder()).run();
		assertSystemOutContains("Line 11");
		assertSystemOutDoesntContain("Line 10");
	}

	@Test
	public void testWarningFromPlacingExpecationOnWrongSide() throws Throwable {
		final String[] message = new String[1];
		stubAllKoans(Arrays.asList(new WrongExpectationOrderKoan()));
		Logger.getLogger(KoanSuiteRunner.class.getSimpleName()).addHandler(
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
		new KoanSuiteRunner(new CommandLineArgumentBuilder()).run();
		assertEquals(
				new StringBuilder(
						WrongExpectationOrderKoan.class.getSimpleName())
						.append(".expectationOnLeft ")
						.append(EXPECTATION_LEFT_ARG).toString(), message[0]);
	}

	@Test
	public void testNoWarningFromPlacingExpecationOnRightSide()
			throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		Logger.getLogger(KoanSuiteRunner.class.getSimpleName()).addHandler(
				new Handler() {
					@Override
					public void close() throws SecurityException {
					}

					@Override
					public void flush() {
					}

					@Override
					public void publish(LogRecord arg0) {
						fail("No logging necessary when koan passes, otherwise - logging is new, adjust accordingly.");
					}
				});
		new KoanSuiteRunner().run();
	}

	public static class WrongExpectationOrderKoan {
		@Koan
		public void expectationOnLeft() {
			com.sandwich.util.Assert.assertEquals(__, false);
		}
	}
}

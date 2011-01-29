package com.sandwich.koans;

import static com.sandwich.koans.KoanConstants.*;
import static com.sandwich.koans.KoanConstants.COMPLETE_CHAR;
import static com.sandwich.koans.KoanConstants.EOL;
import static com.sandwich.koans.KoanConstants.FAILING_SUITES;
import static com.sandwich.koans.KoanConstants.HEADER_LINE;
import static com.sandwich.koans.KoanConstants.INCOMPLETE_CHAR;
import static com.sandwich.koans.KoanConstants.PASSING_SUITES;
import static com.sandwich.koans.KoanConstants.PROGRESS;
import static com.sandwich.koans.KoanConstants.PROGRESS_BAR_START;
import static com.sandwich.koans.KoanConstants.PROGRESS_BAR_WIDTH;
import static com.sandwich.koans.KoanConstants.VERSION;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;


public class KoanSuiteRunnerTest extends CommandLineTestCase {
	
	@Test
	public void testThatKoanSuiteOrderingIsRespected() throws Exception {
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
			@Override void printResult(Map<Object, List<Method>> koans, KoansResult result){
				orderInvoked.add(3);
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
			@Override void printResult(Map<Object, List<Method>> koans, KoansResult r){
				result[0] = r;
			}
		}.run();
		assertEquals(result[0].failingCase, AllKoans.getKoans().get(0));
	}
	
	@Test	/** Ensures that koans are ready for packaging & distribution */
	public void testKoanSuiteRunner_allKoansFail() throws Exception {
		final KoansResult[] result = new KoansResult[]{null};
		new KoanSuiteRunner(){
			@Override void printResult(Map<Object, List<Method>> koans, KoansResult r){
				result[0] = r;
			}
		}.run();
		String message = "Not all koans need solving! Each should ship in a failing state.";
		assertEquals(message, AllKoans.getKoans().get(0), 	result[0].failingCase);
		assertEquals(message, 0, 							result[0].getNumberPassing());
	}
	
	@Test
	public void testKoanDescriptionAppearsOnFailure() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains("fake_description");
	}
	
	@Test
	public void testKoanHintIsHelpful() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(new StringBuilder(
				INVESTIGATE_IN_THE).append(
				" ").append(
				OneFailingKoanDifferentName.class.getSimpleName()).append(
				" class's ").append(
				OneFailingKoanDifferentName.class.getDeclaredMethods()[0].getName()).append(
				" method.").append(
				EOL).toString());
	}
	
	@Test
	public void testEncouragement() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoan.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(new StringBuilder(
				CONQUERED).append(
				" 0 ").append(
				OUT_OF).append(
				" 1 ").append(
				KOAN).append(
				"! ").append(
				ENCOURAGEMENT).append(
				EOL).toString());
	}
	
	@Test
	public void testOneHundredPercentSuccessReward() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(ALL_SUCCEEDED);
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_header() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[]{OnePassingKoan.class, OneFailingKoan.class}));
		KoanSuiteRunner.main();
		assertSystemOutLineEquals(0, HEADER_LINE);
		assertSystemOutLineEquals(1, "");
		assertSystemOutLineEquals(2, "*   "+APP_NAME+" "+VERSION+"   *");
		assertSystemOutLineEquals(3, "");
		assertSystemOutLineEquals(4, HEADER_LINE);
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_passingSuites() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class,
				OnePassingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(PASSING_SUITES+" OnePassingKoan, OnePassingKoanDifferentName");
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_failingSuites() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoan.class,
				OneFailingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(FAILING_SUITES+" OneFailingKoan, OneFailingKoanDifferentName");
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_failingAndPassingSuites() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class,
				OneFailingKoan.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(PASSING_SUITES+" OnePassingKoan"+EOL+
								FAILING_SUITES+" OneFailingKoan");
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_progressAllPassing() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class,
				OnePassingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		StringBuilder sb = new StringBuilder(PROGRESS)
									.append(EOL)
									.append(PROGRESS_BAR_START);
		for(int i = 0; i < PROGRESS_BAR_WIDTH; i++){ // 100% success
			sb.append(COMPLETE_CHAR);
		}
		sb.append("] 2/2").append(EOL);
		assertSystemOutContains(sb.toString());
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_progressAllFailing() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoan.class,
				OneFailingKoanDifferentName.class }));
		KoanSuiteRunner.main();
		StringBuilder sb = new StringBuilder(
				PROGRESS).append(
				EOL).append(
				PROGRESS_BAR_START);
		for(int i = 0; i < PROGRESS_BAR_WIDTH; i++){ // 100% failed
			sb.append(INCOMPLETE_CHAR);
		}
		sb.append("] 0/2").append(EOL);
		assertSystemOutContains(sb.toString());
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_progressFiftyFifty() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class,
				OneFailingKoan.class }));
		KoanSuiteRunner.main();
		StringBuilder sb = new StringBuilder(
				PROGRESS).append(
				EOL).append(
				PROGRESS_BAR_START);
		for(int i = 0; i < PROGRESS_BAR_WIDTH / 2; i++){ // 100% failed
			sb.append(COMPLETE_CHAR);
		}
		for(int i = 0; i < PROGRESS_BAR_WIDTH / 2; i++){ // 100% failed
			sb.append(INCOMPLETE_CHAR);
		}
		sb.append("] 1/2").append(EOL);
		assertSystemOutContains(sb.toString());
	}
	
	@Test
	public void testKoanSuiteRunnerPresentation_whatWentWrongExplanation() throws Exception {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OneFailingKoan.class }));
		KoanSuiteRunner.main();
		assertSystemOutContains(new StringBuilder(
				WHATS_WRONG).append(
				EOL).append(
				"expected:<true> but was:<false>").toString());
	}
	
	static class OnePassingKoan {
		boolean[] invoked;
		public OnePassingKoan(){
			invoked = new boolean[]{false};
		}
		@Koan
		public void koan() {
			invoked[0] = true;
		}
	}
	
	static class OnePassingKoanDifferentName extends OnePassingKoan{}
	
	static class OneFailingKoan {
		@Koan
		public void koan() {
			assertEquals(true, false);
		}
	}
	
	static class OneFailingKoanDifferentName extends OneFailingKoan{
		@Koan(value = "fake_description")
		@Override
		public void koan() {
			assertEquals(true, false);
		}
	}
}

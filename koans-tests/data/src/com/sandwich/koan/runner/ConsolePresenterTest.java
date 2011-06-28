package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.ALL_SUCCEEDED;
import static com.sandwich.koan.constant.KoanConstants.COMPLETE_CHAR;
import static com.sandwich.koan.constant.KoanConstants.CONQUERED;
import static com.sandwich.koan.constant.KoanConstants.ENCOURAGEMENT;
import static com.sandwich.koan.constant.KoanConstants.EOL;
import static com.sandwich.koan.constant.KoanConstants.FAILING_SUITES;
import static com.sandwich.koan.constant.KoanConstants.INCOMPLETE_CHAR;
import static com.sandwich.koan.constant.KoanConstants.INVESTIGATE_IN_THE;
import static com.sandwich.koan.constant.KoanConstants.KOAN;
import static com.sandwich.koan.constant.KoanConstants.OUT_OF;
import static com.sandwich.koan.constant.KoanConstants.PASSING_SUITES;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_START;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_WIDTH;
import static com.sandwich.koan.constant.KoanConstants.WHATS_WRONG;

import java.util.Arrays;

import org.junit.Test;

import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.koan.suite.OneFailingKoan;
import com.sandwich.koan.suite.OneFailingKoanDifferentName;
import com.sandwich.koan.suite.OnePassingKoan;
import com.sandwich.koan.suite.OnePassingKoanDifferentName;

public class ConsolePresenterTest extends CommandLineTestCase {

	@Test
	public void hintPresentation() throws Throwable {
		stubAllKoans(Arrays.asList(new OneFailingKoanDifferentName()));
		new CommandLineArgumentRunner(new CommandLineArgumentBuilder()).run();
		assertSystemOutContains(new StringBuilder(
				INVESTIGATE_IN_THE).append(
				" ").append(
				OneFailingKoanDifferentName.class.getSimpleName()).append(
				" class's ").append(
				OneFailingKoanDifferentName.class.getDeclaredMethod("koanMethod").getName()).append(
				" method.").toString());
		assertSystemOutContains("Line 11 may offer a clue as to how you may progress, now make haste!");
	}
	
	@Test // uncomment enableEncouragement @ the top of ConsolePresenter class
	public void encouragement() throws Throwable {
		if(KoanConstants.ENABLE_ENCOURAGEMENT){
			stubAllKoans(Arrays.asList(new Class<?>[] { 
					OneFailingKoan.class }));
			new CommandLineArgumentRunner().run();
			assertSystemOutContains(new StringBuilder(
					CONQUERED).append(
					" 0 ").append(
					OUT_OF).append(
					" 1 ").append(
					KOAN).append(
					"! ").append(
					ENCOURAGEMENT).toString());
			assertSystemOutDoesntContain(ALL_SUCCEEDED);
		}
	}
	
	@Test
	public void testOneHundredPercentSuccessReward() throws Throwable {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class }));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(ALL_SUCCEEDED);
		assertSystemOutDoesntContain(CONQUERED);
		assertSystemOutDoesntContain(ENCOURAGEMENT);
	}
	
	@Test
	public void passingSuites() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan(), new OnePassingKoanDifferentName()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(PASSING_SUITES+" OnePassingKoan, OnePassingKoanDifferentName");
	}
	
	@Test
	public void failingSuites() throws Throwable {
		stubAllKoans(Arrays.asList(new OneFailingKoan(), new OneFailingKoanDifferentName()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(FAILING_SUITES+" OneFailingKoan, OneFailingKoanDifferentName");
	}
	
	@Test
	public void failingAndPassingSuites() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan(), new OneFailingKoan()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(PASSING_SUITES+" OnePassingKoan"+EOL+
								FAILING_SUITES+" OneFailingKoan");
	}
	
	@Test
	public void progressAllPassing() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		new CommandLineArgumentRunner().run();
		StringBuilder sb = new StringBuilder(PROGRESS).append(" ")
									.append(PROGRESS_BAR_START);
		for(int i = 0; i < PROGRESS_BAR_WIDTH; i++){ // 100% success
			sb.append(COMPLETE_CHAR);
		}
		sb.append("] 1/1").append(EOL);
		assertSystemOutContains(sb.toString());
	}
	
	@Test
	public void progressAllFailing() throws Throwable {
		stubAllKoans(Arrays.asList(new OneFailingKoan(), new OneFailingKoanDifferentName()));
		new CommandLineArgumentRunner().run();
		StringBuilder sb = new StringBuilder(
				PROGRESS).append(" ").append(
				PROGRESS_BAR_START);
		for(int i = 0; i < PROGRESS_BAR_WIDTH; i++){ // 100% failed
			sb.append(INCOMPLETE_CHAR);
		}
		sb.append("] 0/2").append(EOL);
		assertSystemOutContains(sb.toString());
	}
	
	@Test
	public void progressFiftyFifty() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan(), new OneFailingKoan()));
		new CommandLineArgumentRunner().run();
		StringBuilder sb = new StringBuilder(
				PROGRESS).append(" ").append(
				PROGRESS_BAR_START);
		for(int i = 0; i < PROGRESS_BAR_WIDTH / 2; i++){ // 50% succeeded
			sb.append(COMPLETE_CHAR);
		}
		for(int i = 0; i < PROGRESS_BAR_WIDTH / 2; i++){ // 50% failed
			sb.append(INCOMPLETE_CHAR);
		}
		sb.append("] 1/2").append(EOL);
		assertSystemOutContains(sb.toString());
	}
	
	@Test
	public void whatWentWrongExplanation() throws Throwable {
		stubAllKoans(Arrays.asList(new OneFailingKoan()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(new StringBuilder(
				WHATS_WRONG).append(
				EOL).append(
				"expected:<true> but was:<false>").toString());
	}
	
}

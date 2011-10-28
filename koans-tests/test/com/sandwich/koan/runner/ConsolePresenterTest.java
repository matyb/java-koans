package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.COMPLETE_CHAR;
import static com.sandwich.koan.constant.KoanConstants.EOL;
import static com.sandwich.koan.constant.KoanConstants.INCOMPLETE_CHAR;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_START;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_WIDTH;

import java.util.Arrays;

import org.junit.Test;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.koan.suite.OneFailingKoan;
import com.sandwich.koan.suite.OneFailingKoanDifferentName;
import com.sandwich.koan.suite.OnePassingKoan;
import com.sandwich.koan.suite.OnePassingKoanDifferentName;
import com.sandwich.util.Strings;

public class ConsolePresenterTest extends CommandLineTestCase {

	@Test
	public void hintPresentation() throws Throwable {
		stubAllKoans(Arrays.asList(new OneFailingKoanDifferentName()));
		new CommandLineArgumentRunner(new CommandLineArgumentBuilder()).run();
		assertSystemOutContains(new StringBuilder(
				Strings.getMessage("investigate")).append(
				": ").append(
				OneFailingKoanDifferentName.class.getSimpleName()).append(
				" class's ").append(
				OneFailingKoanDifferentName.class.getDeclaredMethod("koanMethod").getName()).append(
				" method.").toString());
		assertSystemOutContains("Line 11 may offer a clue as to how you may progress, now make haste!");
	}
	
	@Test // uncomment enableEncouragement @ the top of ConsolePresenter class
	public void encouragement() throws Throwable {
		if(ApplicationSettings.isEncouragementEnabled()){
			stubAllKoans(Arrays.asList(new Class<?>[] { 
					OneFailingKoan.class }));
			new CommandLineArgumentRunner().run();
			assertSystemOutContains(new StringBuilder(
					Strings.getMessage("you_have_conquered")).append(
					" 0 ").append(
					Strings.getMessage("out_of")).append(
					" 1 ").append(
					Strings.getMessage("koan")).append(
					"! ").append(
					Strings.getMessage("encouragement")).toString());
			assertSystemOutDoesntContain(Strings.getMessage("all_koans_succeeded"));
		}
	}
	
	@Test
	public void testOneHundredPercentSuccessReward() throws Throwable {
		stubAllKoans(Arrays.asList(new Class<?>[] { 
				OnePassingKoan.class }));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(Strings.getMessage("all_koans_succeeded"));
		assertSystemOutDoesntContain(Strings.getMessage("you_have_conquered"));
		assertSystemOutDoesntContain(Strings.getMessage("encouragement"));
	}
	
	@Test
	public void passingSuites() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan(), new OnePassingKoanDifferentName()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(Strings.getMessage("passing_suites")+": OnePassingKoan, OnePassingKoanDifferentName");
	}
	
	@Test
	public void failingSuites() throws Throwable {
		stubAllKoans(Arrays.asList(new OneFailingKoan(), new OneFailingKoanDifferentName()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(Strings.getMessage("remaining_suites")+": OneFailingKoan, OneFailingKoanDifferentName");
	}
	
	@Test
	public void failingAndPassingSuites() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan(), new OneFailingKoan()));
		new CommandLineArgumentRunner().run();
		assertSystemOutContains(Strings.getMessage("passing_suites")+": OnePassingKoan"+EOL+
				Strings.getMessage("remaining_suites")+": OneFailingKoan");
	}
	
	@Test
	public void progressAllPassing() throws Throwable {
		stubAllKoans(Arrays.asList(new OnePassingKoan()));
		new CommandLineArgumentRunner().run();
		StringBuilder sb = new StringBuilder(Strings.getMessage("progress")).append(" ")
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
				Strings.getMessage("progress")).append(" ").append(
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
				Strings.getMessage("progress")).append(" ").append(
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
				Strings.getMessage("what_went_wrong")).append(
				": ").append(
				EOL).append(
				"expected:<true> but was:<false>").toString());
	}
	
}

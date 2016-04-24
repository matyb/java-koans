package com.sandwich.koan.ui;

import static com.sandwich.koan.constant.KoanConstants.EOL;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_END;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_START;
import static com.sandwich.koan.constant.KoanConstants.PROGRESS_BAR_WIDTH;

import java.util.List;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.Strings;

public class ConsolePresenter extends AbstractSuitePresenter {
	
	private static final int NUMBER_OF_LINES_TO_CLEAR_CONSOLE = 80;
	
	public void clearMessages(){
		for(int i = 0; i < NUMBER_OF_LINES_TO_CLEAR_CONSOLE; i++){
			ApplicationUtils.getPresenter().displayMessage(" ");
		}
	}
	
	protected void displayHeader(KoanSuiteResult result){
	}
	
	protected void displayPassingFailing(KoanSuiteResult result) {
		StringBuilder sb = new StringBuilder();
		appendLabeledClassesList(Strings.getMessage("passing_suites")+":", result.getPassingSuites(), sb);
		appendLabeledClassesList(Strings.getMessage("remaining_suites")+":", result.getRemainingSuites(), sb);
		sb.append(EOL).append("Edit & save a koan").append(
		        ApplicationSettings.isInteractive() ? 
		                " to test your progress, or enter '" + ApplicationSettings.getExitChar() +"' to exit." : 
		                ", then rerun to test your progress");
		displayMessage(sb.toString());
	}
	
	private void appendLabeledClassesList(String suiteType, List<String> suites, StringBuilder sb) {
		if(suites == null || suites.isEmpty()){
			return;
		}
		sb.append(suiteType+' ');
		for(String c : suites){
			sb.append(c);
			if(suites.indexOf(c) != suites.size() - 1){
				sb.append(", ");
			}
		}
		sb.append(EOL);
	}
	
	protected void displayChart(KoanSuiteResult result) {
		StringBuilder sb = new StringBuilder(Strings.getMessage("level")).append(": ").append(result.getLevel()).append(EOL);
		int numberPassing = result.getNumberPassing();
		int totalKoans = result.getTotalNumberOfKoans();
		double percentPassing = ((double) numberPassing) / ((double) totalKoans);
		int percentScaledToFifty = (int) (percentPassing * PROGRESS_BAR_WIDTH);
		sb.append(Strings.getMessage("progress")).append(" ").append(PROGRESS_BAR_START);
		for (int i = 0; i < PROGRESS_BAR_WIDTH; i++) {
			if (i < percentScaledToFifty) {
				sb.append(KoanConstants.COMPLETE_CHAR);
			} else {
				sb.append(KoanConstants.INCOMPLETE_CHAR);
			}
		}
		sb.append(PROGRESS_BAR_END);
		sb.append(' ');
		sb.append(numberPassing).append("/").append(totalKoans);
		displayMessage(sb.toString());
	}
	
	@Override
	protected void displayAllSuccess(KoanSuiteResult result) {
		displayMessage(new StringBuilder(EOL).append(Strings.getMessage("all_koans_succeeded")).toString());
	}
	
	@Override
	protected void displayOneOrMoreFailure(KoanSuiteResult result) {
		printSuggestion(result);
		String message = result.getMessage();
		StringBuilder sb = new StringBuilder();
		if (ApplicationSettings.isExpectationResultVisible()) {
			sb.append(message == null || message.length() == 0 || !result.displayIncompleteException() ? ""
				: new StringBuilder(Strings.getMessage("what_went_wrong")).append(
									": ").append(
									EOL).append(
									message).append(
									EOL));
		}
		if(ApplicationSettings.isEncouragementEnabled()){ // added noise to console output, and no real value
			int totalKoans = result.getTotalNumberOfKoans();
			int numberPassing = result.getNumberPassing();
			sb.append(				EOL).append(
									Strings.getMessage("you_have_conquered")).append(
									" ").append(
									numberPassing).append(
									" ").append(
									Strings.getMessage("out_of")).append(
									" ").append(
									totalKoans).append(
									" ").append(
									totalKoans != 1 ? Strings.getMessage("koans") : Strings.getMessage("koan")).append(
									"! ").append(
									Strings.getMessage("encouragement"));
		}
		displayMessage(sb.toString());
	}
	
	protected void printSuggestion(KoanSuiteResult result) {
		KoanMethod failedKoan = result.getFailingMethod();
		StringBuilder sb = 	buildLessonLine(failedKoan);
		buildInvestigateLine(sb, result.getFailingCase(),failedKoan.getMethod().getName());
		sb.append(EOL).append(EOL);
		buildLineClue(sb, result);
		displayMessage(sb.toString());
	}

	private StringBuilder buildInvestigateLine(StringBuilder sb, String simpleName,
			String methodName) {
		return sb.append(
				Strings.getMessage("investigate")).append(
				": ").append(
				simpleName).append(
				" class's ").append(
				methodName).append(
				" method.");
	}

	private StringBuilder buildLineClue(StringBuilder sb, KoanSuiteResult result) {
		if(result.getLineNumber() != null && result.getLineNumber().trim().length() != 0){
			sb.append(	Strings.getMessage("line")).append(" ").append(
						result.getLineNumber()).append(" ").append(
						Strings.getMessage("may_offer_clue"));
		}
		return sb;
	}

	private StringBuilder buildLessonLine(KoanMethod failedKoan) {
		String lesson = failedKoan.getLesson();
		if(lesson == null){
			return new StringBuilder(); // no lesson
		}
		return new StringBuilder(lesson).append(EOL).append(EOL);
	}

	public void displayError(String error) {
		System.err.println(error);
	}
	
	public void displayMessage(String message) {
		System.out.println(message);
	}
}

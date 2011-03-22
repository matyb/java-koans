package com.sandwich.koan.ui;

import static com.sandwich.koan.KoanConstants.ALL_SUCCEEDED;
import static com.sandwich.koan.KoanConstants.CONQUERED;
import static com.sandwich.koan.KoanConstants.ENCOURAGEMENT;
import static com.sandwich.koan.KoanConstants.EOL;
import static com.sandwich.koan.KoanConstants.FAILING_SUITES;
import static com.sandwich.koan.KoanConstants.INVESTIGATE_IN_THE;
import static com.sandwich.koan.KoanConstants.KOAN;
import static com.sandwich.koan.KoanConstants.OUT_OF;
import static com.sandwich.koan.KoanConstants.PASSING_SUITES;
import static com.sandwich.koan.KoanConstants.PROGRESS;
import static com.sandwich.koan.KoanConstants.PROGRESS_BAR_END;
import static com.sandwich.koan.KoanConstants.PROGRESS_BAR_START;
import static com.sandwich.koan.KoanConstants.PROGRESS_BAR_WIDTH;
import static com.sandwich.koan.KoanConstants.WHATS_WRONG;

import java.util.List;

import com.sandwich.koan.KoanConstants;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.KoanResult;

public class ConsolePresenter extends AbstractSuitePresenter {
	
	protected void displayHeader(KoanResult result){
	}
	
	protected void displayPassingFailing(KoanResult result) {
		StringBuilder sb = new StringBuilder();
		appendLabeledClassesList(PASSING_SUITES, result.getPassingSuites(), sb);
		appendLabeledClassesList(FAILING_SUITES, result.getRemainingSuites(), sb);
		System.out.println(sb.toString());
	}
	
	private void appendLabeledClassesList(String suiteType, List<Class<?>> suites, StringBuilder sb) {
		if(suites == null || suites.isEmpty()){
			return;
		}
		sb.append(suiteType+' ');
		for(Class<?> c : suites){
			sb.append(c.getSimpleName());
			if(suites.indexOf(c) != suites.size() - 1){
				sb.append(", ");
			}
		}
		sb.append(EOL);
	}
	
	protected void displayChart(KoanResult result) {
		StringBuilder sb = new StringBuilder(KoanConstants.LEVEL).append(result.getLevel()).append(EOL);
		int numberPassing = result.getNumberPassing();
		int totalKoans = result.getTotalNumberOfKoans();
		double percentPassing = ((double) numberPassing) / ((double) totalKoans);
		int percentScaledToFifty = (int) (percentPassing * PROGRESS_BAR_WIDTH);
		sb.append(PROGRESS).append(" ").append(PROGRESS_BAR_START);
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
		System.out.println(sb.toString());
	}
	
	@Override
	protected void displayAllSuccess(KoanResult result) {
		System.out.println(new StringBuilder(EOL).append(ALL_SUCCEEDED).toString());
	}
	
	@Override
	protected void displayOneOrMoreFailure(KoanResult result) {
		printSuggestion(result);
		String message = result.getMessage();
		StringBuilder sb = new StringBuilder(
			message == null || message.length() == 0 || !result.displayIncompleteException() ? ""
				: new StringBuilder(WHATS_WRONG).append(
									EOL).append(
									message).append(
									EOL).append(
									EOL));
		if(KoanConstants.ENABLE_ENCOURAGEMENT){ // added noise to console output, and no real value
			int totalKoans = result.getTotalNumberOfKoans();
			int numberPassing = result.getNumberPassing();
			sb.append(				EOL).append(
									CONQUERED).append(
									" ").append(
									numberPassing).append(
									" ").append(
									OUT_OF).append(
									" ").append(
									totalKoans).append(
									" ").append(
									KOAN).append(
									totalKoans != 1 ? 's' : "").append(
									"! ").append(ENCOURAGEMENT).append(
									EOL);
		}
		System.out.print(sb.toString());
	}
	
	protected void printSuggestion(KoanResult result) {
		KoanMethod failedKoan = result.getFailingMethod();
		StringBuilder sb = 	buildLessonLine(failedKoan);
		sb.append(EOL).append(EOL);
		buildInvestigateLine(sb, result.getFailingCase().getSimpleName(),failedKoan.getMethod().getName());
		sb.append(EOL).append(EOL);
		buildLineClue(sb, result);
		System.out.println(sb.toString());
	}

	private StringBuilder buildInvestigateLine(StringBuilder sb, String simpleName,
			String methodName) {
		return sb.append(
				INVESTIGATE_IN_THE).append(
				" ").append(
				simpleName).append(
				" class's ").append(
				methodName).append(
				" method.");
	}

	private StringBuilder buildLineClue(StringBuilder sb, KoanResult result) {
		if(result.getLineNumber() != null && !result.getLineNumber().trim().isEmpty()){
			sb.append(	"Line ").append(
						result.getLineNumber()).append(
						" may offer a clue as to how you may progress, now make haste!").append(
						EOL);
		}
		return sb;
	}

	private StringBuilder buildLessonLine(KoanMethod failedKoan) {
		return new StringBuilder(failedKoan.getLesson());
	}
}

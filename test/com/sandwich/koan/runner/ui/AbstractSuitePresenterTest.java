package com.sandwich.koan.runner.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sandwich.koan.KoanResult;
import com.sandwich.koan.ui.AbstractSuitePresenter;

public class AbstractSuitePresenterTest {

	@Test
	public void testForwardingOneHundredPercentSuccess() throws Exception {
		final int state[] = new int[]{0};
		AbstractSuitePresenter presenter = new AbstractSuitePresenter() {
			public void displayHeader(KoanResult result) {
				assertEquals(0, state[0]);
				state[0] = 1;
			}
			public void displayPassingFailing(KoanResult result) {
				assertEquals(1, state[0]);
				state[0] = 2;
			}
			public void displayChart(KoanResult result) {
				assertEquals(2, state[0]);
				state[0] = 3;
			}
			public void displayAllSuccess(KoanResult result) {
				assertEquals(3, state[0]);
				state[0] = 4;
			}
			public void displayOneOrMoreFailure(KoanResult result) {
				fail();
			}
		};
		presenter.displayResult(new KoanResult("", 0, 0, null, null, null, null, null, null){
			@Override public boolean isAllKoansSuccessful(){
				return true;
			}
		});
		assertEquals(4, state[0]);
	}
	
	@Test
	public void testForwardingOneOrMoreFails() throws Exception {
		final int state[] = new int[]{0};
		AbstractSuitePresenter presenter = new AbstractSuitePresenter() {
			public void displayHeader(KoanResult result) {
				assertEquals(0, state[0]);
				state[0] = 1;
			}
			public void displayPassingFailing(KoanResult result) {
				assertEquals(1, state[0]);
				state[0] = 2;
			}
			public void displayChart(KoanResult result) {
				assertEquals(2, state[0]);
				state[0] = 3;
			}
			public void displayAllSuccess(KoanResult result) {
				fail();
			}
			public void displayOneOrMoreFailure(KoanResult result) {
				assertEquals(3, state[0]);
				state[0] = 4;
			}
		};
		presenter.displayResult(new KoanResult("", 0, 0, null, null, null, null, null, null){
			@Override public boolean isAllKoansSuccessful(){
				return false;
			}
		});
		assertEquals(4, state[0]);
	}
}

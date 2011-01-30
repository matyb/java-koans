package com.sandwich.koan.runner.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sandwich.koan.KoansResult;
import com.sandwich.koan.runner.ui.AbstractSuitePresenter;

public class AbstractSuitePresenterTest {

	@Test
	public void testForwardingOneHundredPercentSuccess() throws Exception {
		final int state[] = new int[]{0};
		AbstractSuitePresenter presenter = new AbstractSuitePresenter() {
			public void displayHeader() {
				assertEquals(0, state[0]);
				state[0] = 1;
			}
			public void displayPassingFailing(KoansResult result) {
				assertEquals(1, state[0]);
				state[0] = 2;
			}
			public void displayChart(KoansResult result) {
				assertEquals(2, state[0]);
				state[0] = 3;
			}
			public void displayAllSuccess(KoansResult result) {
				assertEquals(3, state[0]);
				state[0] = 4;
			}
			public void displayOneOrMoreFailure(KoansResult result) {
				fail();
			}
		};
		presenter.displayResult(new KoansResult(0, 0, null, null, null, null, null, null){
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
			public void displayHeader() {
				assertEquals(0, state[0]);
				state[0] = 1;
			}
			public void displayPassingFailing(KoansResult result) {
				assertEquals(1, state[0]);
				state[0] = 2;
			}
			public void displayChart(KoansResult result) {
				assertEquals(2, state[0]);
				state[0] = 3;
			}
			public void displayAllSuccess(KoansResult result) {
				fail();
			}
			public void displayOneOrMoreFailure(KoansResult result) {
				assertEquals(3, state[0]);
				state[0] = 4;
			}
		};
		presenter.displayResult(new KoansResult(0, 0, null, null, null, null, null, null){
			@Override public boolean isAllKoansSuccessful(){
				return false;
			}
		});
		assertEquals(4, state[0]);
	}
}

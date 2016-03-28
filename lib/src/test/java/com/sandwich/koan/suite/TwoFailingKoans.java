package com.sandwich.koan.suite;

import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class TwoFailingKoans {
	@Koan
	public void koanTwo() {
		assertEquals(true, false);
	}

	@Koan
	public void koanMethod() {
		assertEquals(true, false);
	}
}

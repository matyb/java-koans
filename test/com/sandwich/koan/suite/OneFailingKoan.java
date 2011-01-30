package com.sandwich.koan.suite;

import static org.junit.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class OneFailingKoan {
	@Koan("I'm a failure")
	public void koanMethod() {
		assertEquals(true, false);
	}
}

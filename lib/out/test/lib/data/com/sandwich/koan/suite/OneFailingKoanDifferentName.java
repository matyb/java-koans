package com.sandwich.koan.suite;

import static org.junit.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class OneFailingKoanDifferentName extends OneFailingKoan {
	@Koan
	@Override
	public void koanMethod() {
		assertEquals(true, false);
	}
}

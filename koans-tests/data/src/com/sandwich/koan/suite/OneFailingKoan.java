package com.sandwich.koan.suite;

import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class OneFailingKoan {
	@Koan
	public void koanMethod() {
		assertEquals(true, false);
	}
}

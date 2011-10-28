package com.sandwich.koan.suite;

import static com.sandwich.koan.constant.KoanConstants.__;

import com.sandwich.koan.Koan;

public class WrongExpectationOrderKoan {
	@Koan
	public void expectationOnLeft() {
		com.sandwich.util.Assert.assertEquals(__, false);
	}
}
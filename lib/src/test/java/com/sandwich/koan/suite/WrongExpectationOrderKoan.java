package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;

public class WrongExpectationOrderKoan {
    @Koan
    public void expectationOnLeft() {
        com.sandwich.util.Assert.assertEquals(__, false);
    }
}
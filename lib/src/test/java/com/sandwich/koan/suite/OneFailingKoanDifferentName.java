package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;

import static org.junit.Assert.assertEquals;

public class OneFailingKoanDifferentName extends OneFailingKoan {
    @Koan
    @Override
    public void koanMethod() {
        assertEquals(true, false);
    }
}

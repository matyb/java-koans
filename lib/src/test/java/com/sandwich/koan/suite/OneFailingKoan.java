package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;

import static com.sandwich.util.Assert.assertEquals;

public class OneFailingKoan {
    @Koan
    public void koanMethod() {
        assertEquals(true, false);
    }
}

package com.sandwich.koans;

import static org.junit.Assert.assertEquals;

public abstract class KoanSuite {
	
	public static final String __ = "REPLACE ME";
	
	/*
	 * Forwarding methods, makes KoanResult.message readable for people
	 * completing the koans.
	 */
	public static void assertTrue(Object obj){
		assertEquals(true, obj);
	}
	
	public static void assertFalse(Object obj){
		assertEquals(false, obj);
	}
}

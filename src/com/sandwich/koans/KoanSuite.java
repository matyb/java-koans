package com.sandwich.koans;

public abstract class KoanSuite {
	
	public static final String __ = "REPLACE ME";
	
	/*
	 * Forwarding methods, makes KoanResult.message readable for people
	 * completing the koans.
	 */
	public static void assertTrue(Object obj){
		org.junit.Assert.assertEquals(true, obj);
	}
	
	public static void assertFalse(Object obj){
		org.junit.Assert.assertEquals(false, obj);
	}
	
	public static void assertEquals(int first, Integer second) {
		org.junit.Assert.assertTrue(first == second);
	}
	
	public static void assertEquals(int first, int second) {
		org.junit.Assert.assertTrue(first == second);
	}
	
	public static void assertEquals(char first, char second) {
		org.junit.Assert.assertTrue(first == second);
	}
	
	public static void assertEquals(char first, Character second) {
		org.junit.Assert.assertTrue(first == second);
	}
	
	public static void assertEquals(Object first, Object second) {
		org.junit.Assert.assertEquals(first, second);
	}
}

package com.sandwich.koans.suites;

import static com.sandwich.koans.KoanSuite.__;
import static com.sandwich.koans.KoanSuite.assertFalse;
import static com.sandwich.koans.KoanSuite.assertTrue;
import static org.junit.Assert.assertEquals;

import com.sandwich.koans.Koan;
import com.sandwich.koans.KoanOrder;


@KoanOrder(order=0) 
public class AboutAssertions {

	@Koan
	public void assertBooleanTrue() {
		assertTrue(__); // should be true really
	}

	@Koan
	public void assertBooleanFalse() {
		assertFalse(__); 
	}
	
	@Koan
	public void assertEqualsWithInt() {
		assertEquals(1, __); 
	}
	
	@Koan
	public void meh(){
		throw new AssertionError();
	}
}

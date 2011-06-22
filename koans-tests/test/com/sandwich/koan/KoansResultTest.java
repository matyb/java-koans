package com.sandwich.koan;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sandwich.koan.KoanSuiteResult.KoanResultBuilder;
import com.sandwich.koan.suite.OneFailingKoan;

public class KoansResultTest {

	@Test
	public void testToString() throws Exception {
		KoanResultBuilder builder = new KoanResultBuilder()
			.message("msg")
			.failingCase(OneFailingKoan.class)
			.failingMethod(KoanMethod.getInstance("", OneFailingKoan.class.getDeclaredMethods()[0]))
			.level("1")
			.lineNumber("2")
			.numberPassing(3);
		KoanSuiteResult result = builder.build();
		String string = result.toString();
		assertTrue(string.contains("1"));
		assertTrue(string.contains("2"));
		assertTrue(string.contains("3"));
		assertTrue(string.contains(OneFailingKoan.class.toString()));
		assertTrue(string.contains(OneFailingKoan.class.getDeclaredMethods()[0].getName()));
		assertTrue(string.contains("msg"));
	}
	
}

package com.sandwich.koan;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.koan.result.KoanMethodResult;
import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.koan.result.KoanSuiteResult.KoanResultBuilder;
import com.sandwich.koan.suite.OneFailingKoan;

public class KoansResultTest extends CommandLineTestCase {
	
	@Test
	public void testToString() throws Exception {
		KoanResultBuilder builder = new KoanResultBuilder()
			.remainingCases(Arrays.asList(OneFailingKoan.class.getSimpleName()))
			.methodResult(new KoanMethodResult(KoanMethod.getInstance("", OneFailingKoan.class.getDeclaredMethods()[0]), "msg", "2"))
			.level("1")
			.numberPassing(3);
		KoanSuiteResult result = builder.build();
		String string = result.toString();
		assertTrue(string.contains("1"));
		assertTrue(string.contains("2"));
		assertTrue(string.contains("3"));
		assertTrue(string.contains(OneFailingKoan.class.getSimpleName()));
		assertTrue(string.contains(OneFailingKoan.class.getDeclaredMethods()[0].getName()));
		assertTrue(string.contains("msg"));
	}
	
}

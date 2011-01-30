package com.sandwich.koan;

import org.junit.Test;
import static org.junit.Assert.*;

import com.sandwich.koan.KoansResult;
import com.sandwich.koan.suite.OneFailingKoan;

public class KoansResultTest {

	@Test
	public void testToString() throws Exception {
		String string = new KoansResult(1, 2, OneFailingKoan.class,
				OneFailingKoan.class.getDeclaredMethods()[0], "msg", "3", null, null).toString();
		assertTrue(string.contains("1"));
		assertTrue(string.contains("2"));
		assertTrue(string.contains("3"));
		assertTrue(string.contains(OneFailingKoan.class.toString()));
		assertTrue(string.contains(OneFailingKoan.class.getDeclaredMethods()[0].toString()));
		assertTrue(string.contains("msg"));
	}
	
}

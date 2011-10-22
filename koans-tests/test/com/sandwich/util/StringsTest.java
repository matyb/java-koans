package com.sandwich.util;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

import com.sandwich.koan.LocaleSwitchingTestCase;

public class StringsTest extends LocaleSwitchingTestCase {

	public StringsTest(){
		super(Locale.CHINA);
	}
	
	@Test
	public void testStringsBundleInitializationWithNonDefaultLocale() throws Exception {
		assertNotNull(Strings.createResourceBundle());
	}
	
}

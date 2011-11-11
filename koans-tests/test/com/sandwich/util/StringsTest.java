package com.sandwich.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

import com.sandwich.koan.LocaleSwitchingTestCase;

public class StringsTest extends LocaleSwitchingTestCase {
	
	@Test
	public void testStringsBundleInitializationWithNonDefaultLocale() throws Exception {
		Locale.setDefault(Locale.CHINA);
		assertNotNull(Strings.createResourceBundle());
	}
	
	@Test
	public void testFallsBackToEnglishXmlWhenNoXmlForLocaleIsFound_eventIsLogged(){
		Locale.setDefault(Locale.CHINA);
		assertLogged(Strings.class.getName(), new RBSensitiveLoggerExpectation(){
			@Override
			protected void logCalled(LogRecord record) {
				assertEquals(Level.INFO, record.getLevel());
				String expectation0 = "Your default language is not supported yet. ";
				String msg = record.getMessage();
				assertTrue(msg.startsWith(expectation0));
				// c:\meh\always funny stuff %$#\messages_ze.properties or /User/nix machine/messages_ze.properties...
				assertTrue(msg.contains("messages_zh.properties"));			
			}
		});
	}
	
	@Test
	public void testEnglishXmlWhenXmlForLocaleIsFound_eventIsNotLogged(){
		Locale.setDefault(Locale.US);
		assertLogged(Strings.class.getName(), new RBSensitiveLoggerExpectation(){
			@Override
			protected boolean isLogCallRequired() {
				return false;
			}
		});
	}
	
	@Test
	public void testStringsBundleInitializationWithDefaultLocale() throws Exception {
		Locale.setDefault(Locale.US);
		assertNotNull(Strings.createResourceBundle());
	}
	
	private class RBSensitiveLoggerExpectation extends LoggerExpectation {
		@Override
		protected void invokeImplementation() {
			Strings.createResourceBundle();
		}
	}
	
}

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
				String expectation1 = "messages_zh.properties (No such file or directory)";
				String msg = record.getMessage();
				int exp0EndIndex = expectation0.length();
				assertEquals(expectation0, msg.substring(0, exp0EndIndex));
				int exp1StartIndex = msg.length() - expectation1.length();
				// c:\meh\always funny stuff %$#\messages_ze.properties or /User/nix machine/messages_ze.properties...
				assertTrue(msg.substring(exp0EndIndex, exp1StartIndex).contains(System.getProperty("file.separator")));
				assertEquals(expectation1, msg.substring(exp1StartIndex, msg.length()));
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

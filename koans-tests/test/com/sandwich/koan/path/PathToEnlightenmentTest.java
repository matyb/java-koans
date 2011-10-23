package com.sandwich.koan.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

import com.sandwich.koan.LocaleSwitchingTestCase;

public class PathToEnlightenmentTest extends LocaleSwitchingTestCase {

	@Test
	public void testFallsBackToEnglishXmlWhenNoXmlForLocaleIsFound(){
		Locale.setDefault(Locale.CHINA);
		PathToEnlightenment.xmlToPathTransformer = null;
		assertNotNull(PathToEnlightenment.getXmlToPathTransformer());
	}
	
	@Test
	public void testFallsBackToEnglishXmlWhenNoXmlForLocaleIsFound_eventIsLogged(){
		Locale.setDefault(Locale.CHINA);
		PathToEnlightenment.xmlToPathTransformer = null;
		assertLogged(PathToEnlightenment.class.getName(), new RBSensitiveLoggerExpectation(){
			@Override
			protected void logCalled(LogRecord record) {
				assertEquals(Level.INFO, record.getLevel());
				assertEquals("No path to enlightenment for language "+Locale.getDefault().getLanguage()+".", record.getMessage());
			}
		});
	}
	
	@Test
	public void testEnglishXmlWhenXmlForLocaleIsFound_eventIsNotLogged(){
		Locale.setDefault(Locale.US);
		PathToEnlightenment.xmlToPathTransformer = null;
		assertLogged(PathToEnlightenment.class.getName(), new RBSensitiveLoggerExpectation(){
			protected boolean isLogCallRequired(){
				return false;
			}
		});
	}

	@Test
	public void testEnglishXmlWhenXmlForLocaleIsFound(){
		Locale.setDefault(Locale.US);
		PathToEnlightenment.xmlToPathTransformer = null;
		assertNotNull(PathToEnlightenment.getXmlToPathTransformer());
	}
	
	private class RBSensitiveLoggerExpectation extends LoggerExpectation {
		@Override
		protected void invokeImplementation() {
			PathToEnlightenment.getXmlToPathTransformer();
		}
	}
}

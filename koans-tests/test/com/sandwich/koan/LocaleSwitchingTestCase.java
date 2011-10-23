package com.sandwich.koan;

import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;

public class LocaleSwitchingTestCase {

	private Locale defaultLocale;
		
	@Before
	public void setUp(){
		this.defaultLocale = Locale.getDefault();
	}
	
	@After
	public void tearDown(){
		Locale.setDefault(defaultLocale);
	}
	
	protected void assertLogged(String loggerName, final LoggerExpectation loggerExpectation) {
		Logger logger = Logger.getLogger(loggerName);
		final boolean[] called = new boolean[]{false};
		final Handler handler = new Handler(){
			@Override public void close() throws SecurityException {}
			@Override public void flush() {}
			@Override public void publish(final LogRecord record) {
				loggerExpectation.logCalled(record);
				called[0] = true;
			}
		};
		logger.addHandler(handler);
		try{
			loggerExpectation.invokeImplementation();
			if(loggerExpectation.isLogCallRequired() && !called[0]){
				fail("The logger was never called, it should have been.");
			}
		}finally{
			logger.removeHandler(handler);
		}
	}
	
	protected class LoggerExpectation {
		protected void logCalled(LogRecord record){
			fail("Unexpected logging event: "+record);
		}
		protected boolean isLogCallRequired(){
			return true;
		}
		protected void invokeImplementation(){
			fail("No invocation definition defined.");
		}
	}
}

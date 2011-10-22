package com.sandwich.koan;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;

public class LocaleSwitchingTestCase {

	private Locale phonyLocale;
	private Locale defaultLocale;
	
	protected LocaleSwitchingTestCase(Locale locale){
		this.phonyLocale = locale;
	}
	
	@Before
	public void setUp(){
		this.defaultLocale = Locale.getDefault();
		Locale.setDefault(phonyLocale);
	}
	
	@After
	public void tearDown(){
		Locale.setDefault(defaultLocale);
	}
	
}

package com.sandwich.koan.path;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

import com.sandwich.koan.LocaleSwitchingTestCase;

public class PathToEnlightenmentTest extends LocaleSwitchingTestCase {

	public PathToEnlightenmentTest(){
		super(Locale.CHINA);
	}
	
	@Test
	public void testFallsBackToEnglishXmlWhenNoXmlForLocaleIsFound(){
		PathToEnlightenment.xmlToPathTransformer = null;
		assertNotNull(PathToEnlightenment.getXmlToPathTransformer());
	}
	
}

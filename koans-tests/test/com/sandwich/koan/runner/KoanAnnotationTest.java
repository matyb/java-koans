package com.sandwich.koan.runner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.path.CommandLineTestCase;

public class KoanAnnotationTest extends CommandLineTestCase {

	@Test
	public void testThatKoanAnnotationIndicatesMethodToRun() throws Exception {
		final boolean[] invoked = {false};
		Object object = new Object() {
			@SuppressWarnings("unused")
			@Koan
			public void koan() {
				invoked[0] = true;
			}
			@SuppressWarnings("unused")
			public void koanKnockOff(){
				fail();
			}
		};
		super.stubAllKoans(Arrays.asList(object));
		new KoanSuiteRunner().run(); 
		assertTrue(invoked[0]);
	}

}

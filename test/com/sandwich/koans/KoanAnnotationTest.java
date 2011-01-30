package com.sandwich.koans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class KoanAnnotationTest {

	@Test
	public void testThatKoanAnnotationIndicatesMethodToRun() throws Exception {
		final boolean[] invoked = {false};
		Map<Object, List<Method>> koans = new HashMap<Object, List<Method>>();
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
		koans.put(object, Arrays.asList(object.getClass().getDeclaredMethod("koan")));
		new KoanSuiteRunner().runKoans(koans); 
		assertTrue(invoked[0]);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testThatKoanAnnotationDescriptionReferencedAtRuntime() throws Exception {
		final boolean[] invoked = {false};
		final String expected = "koanString";
		Map<Object, List<Method>> koans = new HashMap<Object, List<Method>>();
		Object object = new Object() {
			@Koan(expected)
			public void koan() {
				invoked[0] = true;
			}
		};
		assertEquals(expected, object.getClass().getDeclaredMethod("koan").getAnnotation(Koan.class).value());
	}

}

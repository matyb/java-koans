package com.sandwich.koans.suites;

import static com.sandwich.koans.KoanSuite.__;
import static com.sandwich.koans.KoanSuite.assertFalse;
import static com.sandwich.koans.KoanSuite.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.sandwich.koans.Koan;


/**
 * assertX(...) are static methods from the org.junit.Assert class. They
 * are not prefixed by the class Assert as it's imported statically.
 * 
 * @see the imports section above (it is generally considered bad
 * practice using static imports in production code). 					
 */
public class AboutAssertions {

	@Koan
	public void assertBooleanTrue() {
		assertTrue(__); // should be true really
	}

	@Koan
	public void assertBooleanFalse() {
		assertFalse(__); 
	}
	
	@Koan
	public void assertNullObject(){
		assertNull(__);
	}
	
	/**
	 * TODO introduce instantiation, constructors
	 */
	@Koan
	public void assertNotNullObject(){
		assertNotNull(null);
	}
	
	/** 
	 * TODO explain arguments (mention they've been used up in prior koans
	 */
	@Koan
	public void assertEqualsWithInt() {
		assertEquals(1, __); 	// <- generally, when using an assertXXX methods,
								// expectation is on the left and it is best 
								// practice to use a String for the first arg 
								// indication what has failed
	}
	
	/**
	 * TODO finish explaining
	 */
	@Koan
	public void assertSameInstance(){
		Integer same = new Integer(1);
		Integer __ 	 = new Integer(1);
		assertSame(same, __);
	}
	
	@Koan
	public void assertNotSameInstance(){
		Integer same = new Integer(1);
		Integer __   = same;
		assertNotSame(same, __); 
	}
	
	@Koan
	public void assertThatUsingMatcher(){
		final Boolean __ = Boolean.FALSE;
		final Boolean expected = Boolean.TRUE;
		assertThat(__, new BaseMatcher<Boolean>(){
			@Override
			public boolean matches(Object firstParamInAssertThat) {
				return expected.equals(firstParamInAssertThat);
			}
			public void describeTo(Description descriptionOfFailedMatch) {
				// TODO consider breaking Description into it's own koan
				descriptionOfFailedMatch.appendValue(expected);
			}
		});
	}
}
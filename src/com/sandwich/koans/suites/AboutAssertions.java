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
import com.sandwich.koans.KoanOrder;


/**
 * assertX(...) are static methods from the org.junit.Assert class. They
 * are not prefixed by the class Assert as it's imported statically.
 * 
 * @see the imports section above (it is generally considered bad
 * practice using static imports in production code). 					
 */
@KoanOrder(order=0) 
public class AboutAssertions {

	@Koan(order=0)
	public void assertBooleanTrue() {
		assertTrue(__); // should be true really
	}

	@Koan(order=1)
	public void assertBooleanFalse() {
		assertFalse(__); 
	}
	
	@Koan(order=2)
	public void assertNullObject(){
		assertNull(__);
	}
	
	@Koan(order=3)
	public void assertNotNullObject(){
		Object __ = null;
		assertNotNull(__);
	}
	
	@Koan(order=4)
	public void assertEqualsWithInt() {
		assertEquals(1, __);
	}
	
	@Koan(order=5)
	public void assertSameInstance(){
		Integer same = new Integer(1);
		Integer __ 	 = new Integer(1);
		assertSame(same, __);
	}
	
	@Koan(order=6)
	public void assertNotSameInstance(){
		Integer same = new Integer(1);
		Integer __   = same;
		assertNotSame(same, __); 
	}
	
	@Koan(order=7)
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
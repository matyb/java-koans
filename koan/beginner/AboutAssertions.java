package beginner;

import static com.sandwich.koan.KoanConstants.__;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.sandwich.koan.Koan;


/**
 * assertX(...) are static methods from the org.junit.Assert class. They
 * are not prefixed by the class Assert as it's imported statically.
 * 
 * @see the imports section above (it is generally considered bad
 * practice using static imports in production code). 					
 */
public class AboutAssertions {

	@Koan("The __ are an attempt to communicate the need to fill in an answer.\n" +
	    	"Judging by context, what should __ be replaced with?")
	public void assertBooleanTrue() {
		assertTrue(__); // should be true really
	}

	@Koan("Like the prior koan. Ponder if you will, the power of simple assertions\n" +
			"when verifying an object's behavior.")
	public void assertBooleanFalse() {
		assertFalse(__); 
	}
	
	@Koan("A keyword in java to represent an unitialized reference is 'null'. " +
			"There are times when something should be null, and this assertion can prove that.")
	public void assertNullObject(){
		assertNull(__);
	}
	
	/**
	 * TODO introduce instantiation, constructors
	 */
	@Koan("Sometimes you merely wish to assert an object is not null. This assertion should be used" +
			" sparingly, often a more specific assertion is appropriate.")
	public void assertNotNullObject(){
		assertNotNull(null); // anything other than null should pass here...
	}
	
	/** 
	 * TODO explain arguments (mention they've been used in prior koans)
	 */
	@Koan("Like the prior assertions, only this one invokes equals method on the 2nd to last argument," +
			"\nin this case, 1. This will blow up if the last two arguments are not .equal(...)")
	public void assertEqualsWithDescriptiveMessage() {
		// Generally, when using an assertXXX methods, expectation is on the 
		// left and it is best practice to use a String for the first arg 
		// indication what has failed
		assertEquals("A message as the first arg in assertEquals " +
				"invocations, will appear when the assertion fails.\r", 
				1, __); 	
	}
	
	/**
	 * TODO finish explaining
	 */
	@Koan("An object may equal another object, but it will never be the same as another object.\n" +
			"In other words same is only the same as same.")
	public void assertSameInstance(){
		Integer same = new Integer(1);
		assertSame(same, __);
	}
	
	@Koan("Notice the same instance has been reassigned. Both same and sameReference refer to the same\n" +
			"Integer instance. If sameReference were a new Object() of any type [hint!] this would pass.")
	public void assertNotSameInstance(){
		Integer same 			= new Integer(1);
		Integer sameReference   = same;
		assertNotSame(same, sameReference); 
	}
	
	@Koan("For maximum code reuse - one can write their own assert methods\n" +
			"that reuse matchers for situations specific to your application.")
	public void assertThatUsingMatcher(){
		final Boolean __ = Boolean.FALSE; // <- hint!
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
	
	/*
	 * END KOANS
	 */
	
	/*
	 * Forwarding methods, makes KoanResult.message readable for people
	 * completing the koans.
	 */
	static void assertTrue(Object obj){
		org.junit.Assert.assertEquals(true, obj);
	}
	
	static void assertFalse(Object obj){
		org.junit.Assert.assertEquals(false, obj);
	}
}
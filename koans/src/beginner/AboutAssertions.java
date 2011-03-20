package beginner;

import static com.sandwich.koan.Assert.assertEquals;
import static com.sandwich.koan.Assert.assertFalse;
import static com.sandwich.koan.Assert.assertNotNull;
import static com.sandwich.koan.Assert.assertNotSame;
import static com.sandwich.koan.Assert.assertNull;
import static com.sandwich.koan.Assert.assertSame;
import static com.sandwich.koan.Assert.assertTrue;
import static com.sandwich.koan.KoanConstants.__;

import com.sandwich.koan.Koan;


/**
 * assertX(...) are static methods from the org.junit.Assert class. They
 * are not prefixed by the class Assert as it's imported statically.
 * 
 * @see the imports section above (it is generally considered bad
 * practice using static imports in production code). 					
 */
public class AboutAssertions {

	@Koan() 
	public void assertBooleanTrue() {
		assertTrue(__); // should be true really
	}

	@Koan()
	public void assertBooleanFalse() {
		assertFalse(__); 
	}
	
	@Koan()
	public void assertNullObject(){
		assertNull(__);
	}
	
	@Koan()
	public void assertNotNullObject(){
		assertNotNull(null); // anything other than null should pass here...
	}
	
	@Koan()
	public void assertEqualsWithDescriptiveMessage() {
		// Generally, when using an assertXXX methods, expectation is on the 
		// left and it is best practice to use a String for the first arg 
		// indication what has failed
		assertEquals("A message as the first arg in assertEquals " +
				"invocations, will appear when the assertion fails.\r", 
				1, __); 	
	}
	
	@Koan()
	public void assertSameInstance(){
		Integer same = new Integer(1);
		assertSame(same, __);
	}
	
	@Koan()
	public void assertNotSameInstance(){
		Integer same 			= new Integer(1);
		Integer sameReference   = same;
		assertNotSame(same, sameReference); 
	}
}
package beginner;

// FYI - usually bad practice to import statically, but can make code cleaner

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.*;

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
    public void assertNullObject() {
        assertNull(__);
    }

    @Koan
    public void assertNotNullObject() {
        assertNotNull(null); // anything other than null should pass here...
    }

    @Koan
    public void assertEqualsUsingExpression() {
        assertTrue("Hello World!".equals(__));
    }

    @Koan
    public void assertEqualsWithBetterFailureMessage() {
        assertEquals(1, __);
    }

    @Koan
    public void assertEqualsWithDescriptiveMessage() {
        // Generally, when using an assertXXX methods, expectation is on the
        // left and it is best practice to use a String for the first arg
        // indication what has failed
        assertEquals("The answer to 'life the universe and everything' should be 42", 42, __);
    }

    @Koan
    public void assertSameInstance() {
        Object same = new Integer(1);
        Object sameReference = __;
        assertSame(same, sameReference);
    }

    @Koan
    public void assertNotSameInstance() {
        Integer same = new Integer(1);
        Integer sameReference = same;
        assertNotSame(same, sameReference);
    }
}

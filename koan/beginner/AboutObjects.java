package beginner;

import static com.sandwich.koan.KoanConstants.__;
import static org.junit.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class AboutObjects {

	@Koan("An Object instance should NEVER equal null keyword. This applies to all\n" +
			"subclasses (everything exception primitives subclass Object).")
	public void objectEqualsNull(){
		// does a new object instance equal the null keyword?
		assertEquals(new Object().equals(null), __);
	}
	
	@Koan("An Object instance should equal itself. This too applies to all subclasses of Object.")
	public void objectEqualsSelf(){
		Object obj = new Object();
		// does a new object equal itself?
		assertEquals(obj.equals(obj), __);
	}
	
	@Koan("An object should equal itself, even if referenced from another variable name.")
	public void objectIdentityEqualityIsTrueWhenReferringToSameObject(){
		Object objectReference = new Object();
		Object referenceToSameObject = objectReference;
		// does a new object == itself?
		assertEquals(objectReference == referenceToSameObject, __);
	}
	
	@Koan("Integer, and many other classes implement equals logically, in other words, they\n" +
			"compare properties of each other and not just identity.")
	public void subclassesEqualsMethodIsLooserThanDoubleEquals(){
		Integer integer0 = new Integer(0);
		Integer integer1 = new Integer(0);
		assertEquals(integer0.equals(integer1), __);
	}
	
	@Koan("Double equals operator (==) does not invoke equal, it will evaluate to true if both\n" +
			"references refer to the same object or primitive.")
	public void doubleEqualsOperatorEvalutesToTrueOnlyWithSameInstance(){
		Integer integer0 = new Integer(0);
		Integer integer1 = integer0; // <- assigning same instance to different reference
		assertEquals(integer0 == integer1, __);
	}
	
	@Koan("The inverse of the prior koan, though two objects may be logically equal, they are not\n" +
			"referencing the same object.")
	public void doubleEqualsOperatorEvalutesToFalseWithDifferentInstances(){
		Integer integer0 = new Integer(0);
		Integer integer1 = new Integer(0); // <- new keyword is generating new object instance
		assertEquals(integer0 == integer1, __);
	}
	
	@Koan("How to identify an object at in string form. Should be overridden in any objects with internal state.\n" +
			"Default to string is classname of the instance followed by its hashCode in base 16 (hexadecimal).")
	public void objectToString(){
		Object object = new Object();
		// TODO: Why is it best practice to ALWAYS override toString?
		assertEquals((new StringBuilder()).append(Object.class.getName())
				.append('@')
				.append(Integer.toHexString(object.hashCode())).toString(), __);
	}
	
	@Koan("Java 's string concatenation syntax utilizes addition operator to splice a string with virtually anything.")
	public void toStringConcatenates(){
		final String string = "ha";
		Object object = new Object(){
			@Override public String toString(){
				return string;
			}
		};
		assertEquals(string + object, __);
	}

	@Koan("String concatenation implicitly invokes toString on Objects, unless they are null.\n" +
			"Notice no NullPointerException is thrown.")
	public void toStringIsTestedForNullWhenInvokedImplicitly(){
		String string = "string";
		assertEquals(string+null, __);
	}
}

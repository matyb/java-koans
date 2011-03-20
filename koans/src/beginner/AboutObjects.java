package beginner;

import static com.sandwich.koan.Assert.assertEquals;
import static com.sandwich.koan.KoanConstants.__;

import com.sandwich.koan.Koan;

public class AboutObjects {

	@Koan()
	public void objectEqualsNull(){
		// does a new object instance equal the null keyword?
		assertEquals(new Object().equals(null), __);
	}
	
	@Koan()
	public void objectEqualsSelf(){
		Object obj = new Object();
		// does a new object equal itself?
		assertEquals(obj.equals(obj), __);
	}
	
	@Koan()
	public void objectIdentityEqualityIsTrueWhenReferringToSameObject(){
		Object objectReference = new Object();
		Object referenceToSameObject = objectReference;
		// does a new object == itself?
		assertEquals(objectReference == referenceToSameObject, __);
	}
	
	@Koan()
	public void subclassesEqualsMethodIsLooserThanDoubleEquals(){
		Integer integer0 = new Integer(0);
		Integer integer1 = new Integer(0);
		assertEquals(integer0.equals(integer1), __);
	}
	
	@Koan()
	public void doubleEqualsOperatorEvalutesToTrueOnlyWithSameInstance(){
		Integer integer0 = new Integer(0);
		Integer integer1 = integer0; // <- assigning same instance to different reference
		assertEquals(integer0 == integer1, __);
	}
	
	@Koan()
	public void doubleEqualsOperatorEvalutesToFalseWithDifferentInstances(){
		Integer integer0 = new Integer(0);
		Integer integer1 = new Integer(0); // <- new keyword is generating new object instance
		assertEquals(integer0 == integer1, __);
	}
	
	@Koan()
	public void objectToString(){
		Object object = new Object();
		// TODO: Why is it best practice to ALWAYS override toString?
		assertEquals((new StringBuilder()).append(Object.class.getName())
				.append('@')
				.append(Integer.toHexString(object.hashCode())).toString(), __); //object.toString()
	}
	
	@Koan()
	public void toStringConcatenates(){
		final String string = "ha";
		Object object = new Object(){
			@Override public String toString(){
				return string;
			}
		};
		assertEquals(string + object, __);
	}

	@Koan()
	public void toStringIsTestedForNullWhenInvokedImplicitly(){
		String string = "string";
		assertEquals(string+null, __);
	}
}

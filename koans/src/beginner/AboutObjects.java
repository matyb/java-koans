package beginner;

import com.sandwich.koan.Koan;

import java.text.MessageFormat;

import static com.sandwich.koan.constant.KoanConstants.*;
import static com.sandwich.util.Assert.*;
import static com.sandwich.util.TypeUtils.*;

public class AboutObjects {

	@Koan()
	public void newObjectInstancesCanBeCreatedDirectly() {
		assertEquals(new Object() instanceof Object, __);
	}

	@Koan()
	public void allClassesInheritFromObject() {
		class Foo {}

		Class[] ancestors = ancestors(new Foo());
		assertEquals(ancestors[0], __);
		assertEquals(ancestors[1], __);
	}

	@Koan()
	public void objectToString() {
		Object object = new Object();
		// TODO: Why is it best practice to ALWAYS override toString?
		String expectedToString = MessageFormat.format("{0}@{1}", Object.class.getName(), Integer.toHexString(object.hashCode()));
		assertEquals(expectedToString, __); // hint: object.toString()
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

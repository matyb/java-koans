package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class AboutStrings {

	@Koan
	public void implicitStrings(){
		Class<?> c = "just a plain ole string".getClass();
		assertEquals(c, __);
	}
	
	@Koan
	public void newString(){
		// very rarely if ever should Strings be created via new String() in 
		// practice - generally it is redundant, and done repetitively can be slow
		String string = new String();
		String empty = "";
		assertEquals(string.equals(empty), __);
	}
	
	@Koan
	public void newStringIsRedundant(){
		String stringInstance = "zero";
		String stringReference  = new String(stringInstance);
		assertEquals(stringInstance.equals(stringReference), __);
	}
	
	@Koan
	public void newStringIsNotIdentitical(){
		String stringInstance = "zero";
		String stringReference = new String(stringInstance);
		assertEquals(stringInstance == stringReference, __);
	}
	
	@Koan
	public void stringConcatination(){
		String one = "one";
		String space = " ";
		String two = "two";
		assertEquals(one+space+two, __);
	}
	
	@Koan
	public void efficientStringConcatenation(){
		// the above implicit string concatenation looks nice - but, it is expensive.
		// it creates a new string instance on each concatenation. here's a better alternative:
		assertEquals(new StringBuilder("one").append(" ").append("two").toString(), __);
	}
}

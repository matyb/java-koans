package beginner;

import com.sandwich.koan.Koan;

import java.util.*;
import java.text.MessageFormat;

import static com.sandwich.koan.constant.KoanConstants.*;
import static com.sandwich.util.Assert.*;

public class AboutStrings {

	@Koan
	public void implicitStrings() {
		assertEquals("just a plain ole string".getClass(), __);
	}

	@Koan
	public void newString() {
		// very rarely if ever should Strings be created via new String() in 
		// practice - generally it is redundant, and done repetitively can be slow
		String string = new String();
		String empty = "";
		assertEquals(string.equals(empty), __);
	}

	@Koan
	public void newStringIsRedundant() {
		String stringInstance = "zero";
		String stringReference = new String(stringInstance);
		assertEquals(stringInstance.equals(stringReference), __);
	}

	@Koan
	public void newStringIsNotIdentical() {
		String stringInstance = "zero";
		String stringReference = new String(stringInstance);
		assertEquals(stringInstance == stringReference, __);
	}

	@Koan
	public void stringConcatenation() {
		String one = "one";
		String space = " ";
		String two = "two";
		assertEquals(one + space + two, __);
	}

	@Koan
	public void stringBuilderCanActAsAMutableString() {
		// StringBuilder concatenation looks uglier, but is useful when you need a
		// mutable String like object. It used to be more efficient than using +
		// to concatenate numerous strings, however this is optimized in the compiler now.
		// Usually + concatenation is more appropriate than StringBuilder.
		assertEquals(new StringBuilder("one").append(" ").append("two").toString(), __);
	}

	@Koan
	public void readableStringFormattingWithStringFormat() {
		assertEquals(String.format("%s %s %s", "a", "b", "a"), __);
	}

	@Koan
	public void extraArgumentsToStringFormatGetIgnored() {
		assertEquals(String.format("%s %s %s", "a", "b", "c", "d"), __);
	}

	@Koan
	public void insufficientArgumentsToStringFormatCausesAnError() {
		try {
			String.format("%s %s %s", "a", "b");
			fail("No Exception was thrown!");
		} catch (Exception e) {
			assertEquals(e.getClass(), __);
			assertEquals(e.getMessage(), __);
		}
	}

	@Koan
	public void readableStringFormattingWithMessageFormat() {
		assertEquals(MessageFormat.format("{0} {1} {0}", "a", "b"), __);
	}

	@Koan
	public void extraArgumentsToMessageFormatGetIgnored() {
		assertEquals(MessageFormat.format("{0} {1} {0}", "a", "b", "c"), __);
	}

	@Koan
	public void insufficientArgumentsToMessageFormatDoesNotReplaceTheToken() {
		assertEquals(MessageFormat.format("{0} {1} {0}", "a"), __);
	}

}

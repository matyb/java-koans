package beginner;

import com.sandwich.koan.Koan;

import java.text.MessageFormat;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;
import static com.sandwich.util.Assert.fail;

public class AboutStrings {

    @Koan
    public void implicitStrings() {
        assertEquals("just a plain ole string".getClass(), String.class);
    }

    @Koan
    public void newString() {
        // very rarely if ever should Strings be created via new String() in
        // practice - generally it is redundant, and done repetitively can be slow
        String string = new String();
        String empty = "";
        assertEquals(string.equals(empty), true);
    }

    @Koan
    public void newStringIsRedundant() {
        String stringInstance = "zero";
        String stringReference = new String(stringInstance);
        assertEquals(stringInstance.equals(stringReference), true);
    }

    @Koan
    public void newStringIsNotIdentical() {
        String stringInstance = "zero";
        String stringReference = new String(stringInstance);
        assertEquals(stringInstance == stringReference, false);
    }

    @Koan
    public void stringIsEmpty() {
        assertEquals("".isEmpty(), true);
        assertEquals("one".isEmpty(), false);
        assertEquals(new String().isEmpty(), true);
        assertEquals(new String("").isEmpty(), true);
        assertEquals(new String("one").isEmpty(), false);
    }

    @Koan
    public void stringLength() {
        assertEquals("".length(), 0);
        assertEquals("one".length(), 3);
        assertEquals("the number is one".length(), 17);
    }

    @Koan
    public void stringTrim() {
        assertEquals("".trim(), "");
        assertEquals("one".trim(), "one");
        assertEquals(" one more time".trim(), "one more time");
        assertEquals(" one more time         ".trim(), "one more time");
        assertEquals(" and again\t".trim(), "and again");
        assertEquals("\t\t\twhat about now?\t".trim(), "what about now?");
    }

    @Koan
    public void stringConcatenation() {
        String one = "one";
        String space = " ";
        String two = "two";
        assertEquals(one + space + two, "one two");
        assertEquals(space + one + two, " onetwo");
        assertEquals(two + space + one, "two one");
    }

    @Koan
    public void stringUpperCase() {
        String str = "I am a number one!";
        assertEquals(str.toUpperCase(), "I AM A NUMBER ONE!");
    }

    @Koan
    public void stringLowerCase() {
        String str = "I AM a number ONE!";
        assertEquals(str.toLowerCase(), "i am a number one!");
    }

    @Koan
    public void stringCompare() {
        String str = "I AM a number ONE!";
        assertEquals(str.compareTo("I AM a number ONE!") == 0, true);
        assertEquals(str.compareTo("I am a number one!") == 0, false);
        assertEquals(str.compareTo("I AM A NUMBER ONE!") == 0, false);
    }

    @Koan
    public void stringCompareIgnoreCase() {
        String str = "I AM a number ONE!";
        assertEquals(str.compareToIgnoreCase("I AM a number ONE!") == 0, true);
        assertEquals(str.compareToIgnoreCase("I am a number one!") == 0, true);
        assertEquals(str.compareToIgnoreCase("I AM A NUMBER ONE!") == 0, true);
    }

    @Koan
    public void stringStartsWith() {
        assertEquals("".startsWith("one"), false);
        assertEquals("one".startsWith("one"), true);
        assertEquals("one is the number".startsWith("one"), true);
        assertEquals("ONE is the number".startsWith("one"), false);
    }

    @Koan
    public void stringEndsWith() {
        assertEquals("".endsWith("one"), false);
        assertEquals("one".endsWith("one"), true);
        assertEquals("the number is one".endsWith("one"), true);
        assertEquals("the number is two".endsWith("one"), false);
        assertEquals("the number is One".endsWith("one"), false);
    }

    @Koan
    public void stringSubstring() {
        String str = "I AM a number ONE!";
        assertEquals(str.substring(0), "I AM a number ONE!");
        assertEquals(str.substring(1), " AM a number ONE!");
        assertEquals(str.substring(5), "a number ONE!");
        assertEquals(str.substring(14, 17), "ONE");
        assertEquals(str.substring(7, str.length()), "number ONE!");
    }

    @Koan
    public void stringContains() {
        String str = "I AM a number ONE!";
        assertEquals(str.contains("one"), false);
        assertEquals(str.contains("ONE"), true);
    }

    @Koan
    public void stringReplace() {
        String str = "I am a number ONE!";
        assertEquals(str.replace("ONE", "TWO"), "I am a number TWO!");
        assertEquals(str.replace("I am", "She is"),  "She is a number ONE!");
    }

    @Koan
    public void stringBuilderCanActAsAMutableString() {
        assertEquals(new StringBuilder("one").append(" ").append("two").toString(), "one two");
    }

    @Koan
    public void readableStringFormattingWithStringFormat() {
        assertEquals(String.format("%s %s %s", "a", "b", "a"), "a b a");
    }

    @Koan
    public void extraArgumentsToStringFormatGetIgnored() {
        assertEquals(String.format("%s %s %s", "a", "b", "c", "d"), "a b c");
    }

    @Koan
    public void insufficientArgumentsToStringFormatCausesAnError() {
        try {
            String.format("%s %s %s", "a", "b");
            fail("No Exception was thrown!");
        } catch (Exception e) {
            assertEquals(e.getClass(), java.util.MissingFormatArgumentException.class);
            assertEquals(e.getMessage(), "Format specifier '%s'");
        }
    }

    @Koan
    public void readableStringFormattingWithMessageFormat() {
        assertEquals(MessageFormat.format("{0} {1} {0}", "a", "b"), "a b a");
    }

    @Koan
    public void extraArgumentsToMessageFormatGetIgnored() {
        assertEquals(MessageFormat.format("{0} {1} {0}", "a", "b", "c"), "a b a");
    }

    @Koan
    public void insufficientArgumentsToMessageFormatDoesNotReplaceTheToken() {
        assertEquals(MessageFormat.format("{0} {1} {0}", "a"), "a {1} a");
    }
}

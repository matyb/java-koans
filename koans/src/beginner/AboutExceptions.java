package beginner;

import com.sandwich.koan.Koan;

import java.io.IOException;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutExceptions {

    private void doStuff() throws IOException {
        throw new IOException();
    }

    @Koan
    public void catchCheckedExceptions() {
        String s;
        try {
            doStuff();
            s = "code ran normally";
        } catch (IOException e) {
            s = "exception thrown";
        }
        assertEquals(s, __);
    }

    @Koan
    public void useFinally() {
        String s = "";
        try {
            doStuff();
            s += "code ran normally";
        } catch (IOException e) {
            s += "exception thrown";
        } finally {
            s += " and finally ran as well";
        }
        assertEquals(s, __);
    }

    @Koan
    public void finallyWithoutCatch() {
        String s = "";
        try {
            s = "code ran normally";
        } finally {
            s += " and finally ran as well";
        }
        assertEquals(s, __);
    }

    private void tryCatchFinallyWithVoidReturn(StringBuilder whatHappened) {
        try {
            whatHappened.append("did something dangerous");
            doStuff();
        } catch (IOException e) {
            whatHappened.append("; the catch block executed");
            return;
        } finally {
            whatHappened.append(", but so did the finally!");
        }
    }

    @Koan
    public void finallyIsAlwaysRan() {
        StringBuilder whatHappened = new StringBuilder();
        tryCatchFinallyWithVoidReturn(whatHappened);
        assertEquals(whatHappened.toString(), __);
    }

    @SuppressWarnings("finally")
    // this is suppressed because returning in finally block is obviously a compiler warning
    private String returnStatementsEverywhere(StringBuilder whatHappened) {
        try {
            whatHappened.append("try");
            doStuff();
            return "from try";
        } catch (IOException e) {
            whatHappened.append(", catch");
            return "from catch";
        } finally {
            whatHappened.append(", finally");
            // Think about how bad an idea it is to put a return statement in the finally block
            // DO NOT DO THIS!
            return "from finally";
        }
    }

    @Koan
    public void returnInFinallyBlock() {
        StringBuilder whatHappened = new StringBuilder();
        // Which value will be returned here?
        assertEquals(returnStatementsEverywhere(whatHappened), __);
        assertEquals(whatHappened.toString(), __);
    }

    private void doUncheckedStuff() {
        throw new RuntimeException();
    }

    @Koan
    public void catchUncheckedExceptions() {
        // What do you need to do to catch the unchecked exception?
        doUncheckedStuff();
    }

    @SuppressWarnings("serial")
    static class ParentException extends Exception {
    }

    @SuppressWarnings("serial")
    static class ChildException extends ParentException {
    }

    private void throwIt() throws ParentException {
        throw new ChildException();
    }

    @Koan
    public void catchOrder() {
        String s = "";
        try {
            throwIt();
        } catch (ChildException e) {
            s = "ChildException";
        } catch (ParentException e) {
            s = "ParentException";
        }
        assertEquals(s, __);
    }

    @Koan
    public void failArgumentValidationWithAnIllegalArgumentException() {
        // This koan demonstrates the use of exceptions in argument validation
        String s = "";
        try {
            s += validateUsingIllegalArgumentException(null);
        } catch (IllegalArgumentException ex) {
            s = "caught an IllegalArgumentException";
        }
        assertEquals(s, __);
    }

    @Koan
    public void passArgumentValidationWithAnIllegalArgumentException() {
        // This koan demonstrates the use of exceptions in argument validation
        String s = "";
        try {
            s += validateUsingIllegalArgumentException("valid");
        } catch (IllegalArgumentException ex) {
            s = "caught an IllegalArgumentException";
        }
        assertEquals(s, __);
    }

    private int validateUsingIllegalArgumentException(String str) {
        // This is effective and both the evaluation and the error message
        // can be tailored which can be particularly handy if you're guarding
        // against more than null values
        if (null == str) {
            throw new IllegalArgumentException("str should not be null");
        }
        return str.length();
    }
}

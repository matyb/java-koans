package java8;

import com.sandwich.koan.Koan;

import java.util.function.Function;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutLambdas {

    interface Caps {
        public String capitalize(String name);
    }

    String fieldFoo = "Lambdas";

    @Override
    public String toString() {
        return "CAPS";
    }

    static String str = "";

    //lambda has access to "this"
    Caps lambdaField = s -> this.toString();
    //lambda has access to object methods
    Caps lambdaField2 = s -> toString();

    @Koan
    public void verySimpleLambda() throws InterruptedException {
        Runnable r8 = () -> str = "changed in lambda";
        r8.run();
        assertEquals(str, __);
    }

    @Koan
    public void simpleLambda() {
        Caps caps = (String n) -> {
            return n.toUpperCase();
        };
        String capitalized = caps.capitalize("James");
        assertEquals(capitalized, __);
    }

    @Koan
    public void simpleSuccinctLambda() {
        //parameter type can be omitted,
        //code block braces {} and return statement can be omitted for single statement lambda
        //parameter parenthesis can be omitted for single parameter lambda
        Caps caps = s -> s.toUpperCase();
        String capitalized = caps.capitalize("Arthur");
        assertEquals(capitalized, __);
    }

    @Koan
    public void lambdaField() {
        assertEquals(lambdaField.capitalize(""), __);
    }

    @Koan
    public void lambdaField2() {
        assertEquals(lambdaField2.capitalize(""), __);
    }

    @Koan
    public void effectivelyFinal() {
        //final can be omitted like this:
        /* final */
        String effectivelyFinal = "I'm effectively final";
        Caps caps = s -> effectivelyFinal.toUpperCase();
        assertEquals(caps.capitalize(effectivelyFinal), __);
    }

    @Koan
    public void methodReference() {
        Caps caps = String::toUpperCase;
        String capitalized = caps.capitalize("Gosling");
        assertEquals(capitalized, __);
    }

    @Koan
    public void thisIsSurroundingClass() {
        //"this" in lambda points to surrounding class
        Function<String, String> foo = s -> s + this.fieldFoo + s;
        assertEquals(foo.apply("|"), __);
    }

}


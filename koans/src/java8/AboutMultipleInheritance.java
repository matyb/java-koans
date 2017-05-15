package java8;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutMultipleInheritance {

    interface Human {
        default String sound() {
            return "hello";
        }
    }

    interface Bull {
        default String sound() {
            return "moo";
        }
    }

    class Minotaur implements Human, Bull {
        //both interfaces implement same default method
        //has to be overridden
        @Override
        public String sound() {
            return Bull.super.sound();
        }
    }

    @Koan
    public void multipleInheritance() {
        Minotaur minotaur = new Minotaur();
        assertEquals(minotaur.sound(), "moo");
    }

}

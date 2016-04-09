package beginner;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutInheritance {

    abstract class Animal {
        abstract public String makeSomeNoise();
    }

    class Cow extends Animal {
        @Override
        public String makeSomeNoise() {
            return "Moo!";
        }
    }

    class Dog extends Animal {
        @Override
        public String makeSomeNoise() {
            return "Woof!";
        }

        public boolean canFetch() {
            return true;
        }
    }

    class Puppy extends Dog {
        @Override
        public String makeSomeNoise() {
            return "Squeak!";
        }
        public boolean canFetch() {
            return false;
        }
    }

    @Koan
    public void methodOverloading() {
        Cow bob = new Cow();
        Dog max = new Dog();
        Puppy barney = new Puppy();
        assertEquals(bob.makeSomeNoise(), __);
        assertEquals(max.makeSomeNoise(), __);
        assertEquals(barney.makeSomeNoise(), __);

        assertEquals(max.canFetch(), __);
        assertEquals(barney.canFetch(), __);
        // but can Bob the Cow fetch?
    }

    @Koan
    public void methodOverloadingUsingPolymorphism() {
        Animal bob = new Cow();
        Animal max = new Dog();
        Animal barney = new Puppy();
        assertEquals(bob.makeSomeNoise(), __);
        assertEquals(max.makeSomeNoise(), __);
        assertEquals(barney.makeSomeNoise(), __);
        // but can max or barney (here as an Animal) fetch?
        // try to write it down here
    }

    @Koan
    public void inheritanceHierarchy() {
        Animal someAnimal = new Cow();
        Animal bob = new Cow();
        assertEquals(someAnimal.makeSomeNoise().equals(bob.makeSomeNoise()), __);
        // cow is a Cow, but it can also be an animal
        assertEquals(bob instanceof Animal, __);
        assertEquals(bob instanceof Cow, __);
        // but is it a Puppy?
        assertEquals(bob instanceof Puppy, __);
    }

    @Koan
    public void deeperInheritanceHierarchy() {
        Dog max = new Dog();
        Puppy barney = new Puppy();
        assertEquals(max instanceof Puppy, __);
        assertEquals(max instanceof Dog, __);
        assertEquals(barney instanceof Puppy, __);
        assertEquals(barney instanceof Dog, __);
    }

    // TODO overriding
//
//    abstract class ParentTwo {
//        abstract public Collection<?> doStuff();
//    }
//
//    class ChildTwo extends ParentTwo {
//        public Collection<?> doStuff() {
//            return Collections.emptyList();
//        }
//
//        ;
//    }
//
//    @Koan
//    public void overriddenMethodsMayReturnSubtype() {
//        // What do you need to change in order to get rid of the type cast?
//        // Why does this work?
//        List<?> list = (List<?>) new ChildTwo().doStuff();
//        assertEquals(list instanceof List, __);
//    }
}

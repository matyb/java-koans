package intermediate;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutInnerClasses {

    interface Ignoreable {
        String ignoreAll();
    }

    class Inner {
        public String doStuff() {
            return "stuff";
        }

        public int returnOuter() {
            return x;
        }
    }

    @Koan
    public void creatingInnerClassInstance() {
        Inner someObject = new Inner();
        assertEquals(someObject.doStuff(), __);
    }

    @Koan
    public void creatingInnerClassInstanceWithOtherSyntax() {
        AboutInnerClasses.Inner someObject = this.new Inner();
        assertEquals(someObject.doStuff(), __);
    }

    private int x = 10;

    @Koan
    public void accessingOuterClassMembers() {
        Inner someObject = new Inner();
        assertEquals(someObject.returnOuter(), __);
    }

    @Koan
    public void innerClassesInMethods() {
        class MethodInnerClass {
            int oneHundred() {
                return 100;
            }
        }
        assertEquals(new MethodInnerClass().oneHundred(), __);
        // Where can you use this class?
    }

    class AnotherInnerClass {
        int thousand() {
            return 1000;
        }

        AnotherInnerClass crazyReturn() {
            class SpecialInnerClass extends AnotherInnerClass {
                int thousand() {
                    return 2000;
                }
            }
            ;
            return new SpecialInnerClass();
        }
    }

    @Koan
    public void innerClassesInMethodsThatEscape() {
        AnotherInnerClass ic = new AnotherInnerClass();
        assertEquals(ic.thousand(), __);
        AnotherInnerClass theCrazyIC = ic.crazyReturn();
        assertEquals(theCrazyIC.thousand(), __);
    }

    int theAnswer() {
        return 42;
    }

    @Koan
    public void creatingAnonymousInnerClasses() {
        AboutInnerClasses anonymous = new AboutInnerClasses() {
            int theAnswer() {
                return 23;
            }
        };// <- Why do you need a semicolon here?
        assertEquals(anonymous.theAnswer(), __);
    }

    @Koan
    public void creatingAnonymousInnerClassesToImplementInterface() {
        Ignoreable ignoreable = new Ignoreable() {
            public String ignoreAll() {
                return null;
            }
        }; // Complete the code so that the statement below is correct.
        // Look at the koan above for inspiration
        assertEquals(ignoreable.ignoreAll(), "SomeInterestingString");
        // Did you just created an object of an interface type?
        // Or did you create a class that implemented this interface and
        // an object of that type?
    }

    @Koan
    public void innerClassAndInheritance() {
        Inner someObject = new Inner();
        // The statement below is obvious...
        // Try to change the 'Inner' below to "AboutInnerClasses'
        // Why do you get an error?
        // What does that imply for inner classes and inheritance?
        assertEquals(someObject instanceof Inner, __);
    }

    class OtherInner extends AboutInnerClasses {
    }

    @Koan
    public void innerClassAndInheritanceOther() {
        OtherInner someObject = new OtherInner();
        // What do you expect here?
        // Compare this result with the last koan. What does that mean?
        assertEquals(someObject instanceof AboutInnerClasses, __);
    }

    static class StaticInnerClass {
        public int importantNumber() {
            return 3;
        }
    }

    @Koan
    public void staticInnerClass() {
        StaticInnerClass someObject = new StaticInnerClass();
        assertEquals(someObject.importantNumber(), __);
        // What happens if you try to access 'x' or 'theAnswer' from the outer class?
        // What does this mean for static inner classes?
        // Try to create a sub package of this package which is named 'StaticInnerClass'
        // Does it work? Why not?
    }

    @Koan
    public void staticInnerClassFullyQualified() {
        AboutInnerClasses.StaticInnerClass someObject = new AboutInnerClasses.StaticInnerClass();
        assertEquals(someObject.importantNumber(), __);
    }

}

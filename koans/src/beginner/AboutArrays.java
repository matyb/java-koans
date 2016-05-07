package beginner;

import com.sandwich.koan.Koan;

import java.util.Arrays;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;
import static com.sandwich.util.Assert.fail;

public class AboutArrays {

    @Koan
    public void arraysAreIndexedAtZero() {
        int[] integers = new int[]{1, 2, 3};
        assertEquals(integers[0], __);
        assertEquals(integers[1], __);
        assertEquals(integers[3], __);
    }

    @Koan
    public void arrayIndexOutOfBounds() {
        int[] array = new int[]{1};
        try {
            System.out.println(array[1]);
        } catch (IndexOutOfBoundsException e) {
            fail("you can't access elements which are not in the array");
        }
    }

    @Koan
    public void arrayLengthCanBeChecked() {
        assertEquals(new int[] {} .length, __);
        assertEquals(new int[1].length, __);
        assertEquals(new int[] {1, 2, 3, 7} .length, __);
    }

    @Koan
    public void arraysDoNotConsiderElementsWhenEvaluatingEquality() {
        // arrays utilize default object equality (A == {1} B == {1}, though A
        // and B contain the same thing, the container is not the same
        // referenced array instance...
        assertEquals(new int[]{1}.equals(new int[]{1}), __);
    }

    @Koan
    public void cloneEqualityIsNotRespected() { //!
        int[] original = new int[]{1};
        assertEquals(original.equals(original.clone()), __);
    }

    @Koan
    public void arraysHelperClassEqualsMethodConsidersElementsWhenDeterminingEquality() {
        int[] array0 = new int[]{0};
        int[] array1 = new int[]{0};
        assertEquals(Arrays.equals(array0, array1), __);    // whew - what most people assume
        // about equals in regard to arrays! (logical equality)
    }

    @Koan
    public void arraysAreMutable() {
        final boolean[] oneBoolean = new boolean[]{false};
        assertEquals(oneBoolean[0], __);
        oneBoolean[0] = true;
        assertEquals(oneBoolean[0], __);
    }

    @Koan
    public void waysToCreateAnArray() {
        int[] numbers1 = {1, 2, 3};
        int[] numbers2 = new int[] {1, 2, 3};
        int[] numbers3 = new int[3];
        numbers3[0] = 1; numbers3[1] = 2; numbers3[2] = 3;
        assertEquals(Arrays.equals(numbers1, numbers2), __);
        assertEquals(Arrays.equals(numbers2, numbers3), __);
    }

    @Koan
    public void anArraysHashCodeMethodDoesNotConsiderElements() {
        int[] array0 = new int[]{0};
        int[] array1 = new int[]{0};
        assertEquals(Integer.valueOf(array0.hashCode()).equals(array1.hashCode()), __); // not equal!
        // TODO: ponder the consequences when arrays are used in Hash Collection implementations.
    }

    @Koan
    public void arraysHelperClassHashCodeMethodConsidersElementsWhenDeterminingHashCode() {
        int[] array0 = new int[]{0};
        int[] array1 = new int[]{0};
        // whew - what most people assume about hashCode in regard to arrays!
        assertEquals(Integer.valueOf(Arrays.hashCode(array0)).equals(Arrays.hashCode(array1)), __);
    }
}

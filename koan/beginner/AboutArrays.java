package beginner;

import static org.junit.Assert.assertEquals;
import static com.sandwich.koan.KoanConstants.__;

import java.util.Arrays;

import com.sandwich.koan.Koan;

public class AboutArrays {

	@Koan("Arrays utilize reference equality, they do not consider elements when determining equality.")
	public void arraysDoNotConsiderElementsWhenEvaluatingEquality(){
		assertEquals(new int[] { 1 }.equals(new int[] { 1 }), __); 
		// arrays utilize default object equality (A == {1} B == {1}, though A
		// and B contain the same thing, the container is not the same
		// referenced array instance...
	}
	
	@Koan("The general contract of clone is that: " +
			"Object a == new Object(); a != a.clone(); a.equals(a.clone()). " +
			"Array instances DO NOT honor this contract, despite implementing Cloneable.")
	public void cloneEqualityIs_NotRespected(){ //!
		int[] original = new int[] { 1 };
		assertEquals(original.equals(original.clone()), __);
	}
	
	@Koan("Likewise with hashcode, an array instance's hashCode is that of the array, " +
			"it does not incorporate elements.")
	public void anArraysHashCodeMethodDoesNotConsiderElements(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		assertEquals(Integer.valueOf(array0.hashCode()).equals(array1.hashCode()), __); // not equal!
		// TODO: ponder the consequences when arrays are used in Hash Collection implementations.
	}
	
	@Koan("The Arrays.equals(...) method DOES evaluate elements when determining equality. " +
			"This is called 'Logical Equality'.")
	public void arraysHelperClassEqualsMethodConsidersElementsWhenDeterminingEquality(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		assertEquals(Arrays.equals(array0, array1), __); 	// whew - what most people assume 
		 													// about equals in regard to arrays! (logical equality)
	}

	@Koan("Likewise with hashCode, the Arrays.hashCode(...) method " +
			"DOES consider elements when determining hashCode.")
	public void arraysHelperClassHashCodeMethodConsidersElementsWhenDeterminingHashCode(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		// whew - what most people assume about hashCode in regard to arrays!
		assertEquals(Integer.valueOf(Arrays.hashCode(array0)).equals(Arrays.hashCode(array1)), __);
	}
	
	@Koan("Arrays are always mutable, even when declared final. The final declaration " +
			"prevents reassignment, but does nothing for elements.")
	public void arraysAreMutable(){
		final boolean[] oneBoolean = new boolean[]{false};
		oneBoolean[0] = true;
		assertEquals(oneBoolean[0], __);
	}
	
	@Koan("Arrays contain elements which are indexed by a number starting with zero.")
	public void arraysAreIndexedAtZero(){
		int[] integers = new int[]{1,2};
		assertEquals(integers[0], __);
		assertEquals(integers[1], __);
	}
	
	@Koan("Array instances blow up when referencing an index that doesn't exist.")
	public void arrayIndexOutOfBounds(){
		int[] array = new int[]{1};
		@SuppressWarnings("unused")
		int meh = array[1]; // remember 0 based indexes, 1 is the 2nd element (which doesn't exist)
	}
	
	@Koan("It is often necessary to check the length of an array to ensure an index is valid. " +
			"This is easy with the array's length property.")
	public void arrayLengthCanBeChecked(){
		assertEquals(new int[1].length, __);
	}
	
}

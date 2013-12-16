package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import java.util.Arrays;

import com.sandwich.koan.Koan;

public class AboutArrays {

	@Koan
	public void arraysDoNotConsiderElementsWhenEvaluatingEquality(){
		// arrays utilize default object equality (A == {1} B == {1}, though A
		// and B contain the same thing, the container is not the same
		// referenced array instance...
		assertEquals(new int[] {1}.equals(new int[] {1}), __);
	}
	
	@Koan
	public void cloneEqualityIsNotRespected(){ //!
		int[] original = new int[] { 1 };
		assertEquals(original.equals(original.clone()), __);
	}
	
	@Koan
	public void anArraysHashCodeMethodDoesNotConsiderElements(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		assertEquals(Integer.valueOf(array0.hashCode()).equals(array1.hashCode()), __); // not equal!
		// TODO: ponder the consequences when arrays are used in Hash Collection implementations.
	}
	
	@Koan
	public void arraysHelperClassEqualsMethodConsidersElementsWhenDeterminingEquality(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		assertEquals(Arrays.equals(array0, array1), __); 	// whew - what most people assume 
		 													// about equals in regard to arrays! (logical equality)
	}

	@Koan
	public void arraysHelperClassHashCodeMethodConsidersElementsWhenDeterminingHashCode(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		// whew - what most people assume about hashCode in regard to arrays!
		assertEquals(Integer.valueOf(Arrays.hashCode(array0)).equals(Arrays.hashCode(array1)), __);
	}
	
	@Koan
	public void arraysAreMutable(){
		final boolean[] oneBoolean = new boolean[]{false};
		oneBoolean[0] = true;
		assertEquals(oneBoolean[0], __);
	}
	
	@Koan
	public void arraysAreIndexedAtZero(){
		int[] integers = new int[]{1,2};
		assertEquals(integers[0], __);
		assertEquals(integers[1], __);
	}
	
	@Koan
	public void arrayIndexOutOfBounds(){
		int[] array = new int[]{1};
		@SuppressWarnings("unused")
		int meh = array[1]; // remember 0 based indexes, 1 is the 2nd element (which doesn't exist)
	}
	
	@Koan
	public void arrayLengthCanBeChecked(){
		assertEquals(new int[1].length, __);
	}
	
}

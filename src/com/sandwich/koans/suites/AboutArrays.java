package com.sandwich.koans.suites;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import com.sandwich.koans.Koan;

public class AboutArrays {

	@Koan
	public void arraysStoringAnythingDoNotConsiderElementsWhenEvaluatingEquality(){
		boolean areTheyEqual = true;
		assertEquals(areTheyEqual, new int[] { 1 }.equals(new int[] { 1 })); // not actually equal?!
		Object anObject = new Object();
		assertEquals(areTheyEqual, new Object[] { anObject }.equals(new Object[] { anObject })); // not actually equal?!
		// arrays utilize default object equality (it A = {1} B = {1}, though A
		// and B contain the same thing, the container is not the same
		// referenced array instance
	}
	
	@Koan
	public void cloneEqualityIs_NotRespected(){ //!
		int[] original = new int[] { 1 };
		assertEquals(true, original.equals(original.clone())); // not actually equal?!
		// according to joshua bloch in effective java, the general contract of clone
		// is that: Object a = new Object(); a != a.clone(); a.equals(a.clone());
	}
	
	@Koan
	public void anArraysHashCodeMethodDoesNotConsiderElements(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		assertEquals(true, Integer.valueOf(array0.hashCode()).equals(array1.hashCode())); // not equal!
		// TODO: ponder the consequences when arrays are used in Hash Collection implementations.
	}
	
	@Koan
	public void arraysHelperClassEqualsMethodConsidersElementsWhenDeterminingEquality(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		assertEquals(false, Arrays.equals(array0, array1)); // whew - what most people assume 
		 													// about equals in regard to arrays!
	}

	@Koan
	public void arraysHelperClassHashCodeMethodConsidersElementsWhenDeterminingHashCode(){
		int[] array0 = new int[]{0};
		int[] array1 = new int[]{0};
		// whew - what most people assume about hashCode in regard to arrays!
		assertEquals(false, Integer.valueOf(Arrays.hashCode(array0)).equals(Arrays.hashCode(array1)));
	}
}

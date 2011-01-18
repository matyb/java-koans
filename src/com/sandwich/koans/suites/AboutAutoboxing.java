package com.sandwich.koans.suites;

import static com.sandwich.koans.KoanSuite.__;
import static com.sandwich.koans.KoanSuite.assertFalse;
import static com.sandwich.koans.KoanSuite.assertTrue;
import static com.sandwich.koans.KoanSuite.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.sandwich.koans.Koan;

public class AboutAutoboxing {

	@Koan("Before Java 5, we had to convert primatives to add to collections")
	public void addPrimativesToCollection() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(0, new Integer(42));
		assertEquals(__, list.get(0).intValue());
	}
	
	@Koan("With AutoBoxing, we can _cheat_")
	public void addPrimativesToCollectionWithAutoBoxing() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(0, 42);
		assertEquals(__, list.get(0));
	}
	
	@Koan("With AutoBoxing, we can intermix as well")
	public void migrateYourExistingCodeToAutoBoxingWithoutFear() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(0, new Integer(42));
		assertEquals(__, list.get(0));

		list.add(1, 84);
		assertEquals(__, list.get(1).intValue());
		
	}
	
}

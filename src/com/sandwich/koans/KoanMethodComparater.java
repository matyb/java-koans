package com.sandwich.koans;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.logging.Logger;

class KoanMethodComparater implements Comparator<Method>{
	
	@Override
	public int compare(Method o0, Method o1) {
		Koan order0 = o0.getAnnotation(Koan.class);
		Koan order1 = o1.getAnnotation(Koan.class);
		return compareInts(
				order0 == null ? -1 : order0.order(),
				order1 == null ? 1 : order1.order(),
				o0.getClass().getSimpleName(), o1.getClass().getSimpleName());
	}
	
	private int compareInts(int i0, int i1, String classZeroName, String classOneName){
		int comparison = Integer.valueOf(i0).compareTo(i1);
		if(comparison == 0){
			Logger.getAnonymousLogger().info(
					classZeroName + " and " + classOneName +
					" evaluate to the same " + Koan.class.getSimpleName() + '.' +
					"\nThis will break the order the koans are evaluated and presented " +
					"to users.");
		}
		return comparison;
	}
}

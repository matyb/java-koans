package com.sandwich.koans.suites;

import java.util.Arrays;
import java.util.List;

public class AllKoans {

	@SuppressWarnings("unchecked")
	private static List<Class<?>> koans = Arrays.asList(
			AboutAssertions.class,
			AboutObjects.class,
			AboutMocks.class);
	
	public static List<Class<?>> getKoans(){
		return koans;
	}
	
}

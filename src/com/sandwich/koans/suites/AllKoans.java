package com.sandwich.koans.suites;

import java.util.Arrays;
import java.util.List;

public class AllKoans {

	private static List<Class<?>> koans = Arrays.asList(new Class<?>[]{
			AboutAssertions.class,
			AboutObjects.class,
			AboutAutoboxing.class,
			AboutMocks.class});
	
	public static List<Class<?>> getKoans(){
		return koans;
	}
	
}

package com.sandwich.koans;

import java.util.Arrays;
import java.util.List;

import com.sandwich.koans.suites.AboutArrays;
import com.sandwich.koans.suites.AboutAssertions;
import com.sandwich.koans.suites.AboutAutoboxing;
import com.sandwich.koans.suites.AboutMocks;
import com.sandwich.koans.suites.AboutObjects;

public class AllKoans {

	static List<Class<?>> koans = Arrays.asList(new Class<?>[]{
			AboutAssertions.class,
			AboutObjects.class,
			AboutArrays.class,
			AboutAutoboxing.class,
			AboutMocks.class
	});
	
	public static List<Class<?>> getKoans(){
		return koans;
	}
	
}

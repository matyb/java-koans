package com.sandwich.koan.runner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sandwich.koan.suite.AboutArrays;
import com.sandwich.koan.suite.AboutAssertions;
import com.sandwich.koan.suite.AboutAutoboxing;
import com.sandwich.koan.suite.AboutMocks;
import com.sandwich.koan.suite.AboutObjects;

public abstract class PathToEnlightment {

	static List<Class<?>> createKoansList(){
		return Collections.unmodifiableList(Arrays.asList(new Class<?>[]{
		
				AboutAssertions.class,
				AboutObjects.class,
				AboutArrays.class,
				AboutAutoboxing.class,
				AboutMocks.class
		
		}));
	}
	
	static List<Class<?>> koans = createKoansList();
	
	public static List<Class<?>> getPathToEnlightment(){
		return koans;
	}
	
	private PathToEnlightment(){} // non instantiable
}

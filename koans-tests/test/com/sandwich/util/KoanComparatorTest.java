package com.sandwich.util;

import static com.sandwich.koan.constant.KoanConstants.PROJ_MAIN_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.SOURCE_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.TESTS_FOLDER;
import static com.sandwich.koan.constant.KoanConstants.PROJ_TESTS_FOLDER;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;

public class KoanComparatorTest {
	
	private static final String ORIGINAL_KOANS_PROJ_DIR = 	PROJ_MAIN_FOLDER;
	private static final String ORIGINAL_SRC_DIR 		= 	SOURCE_FOLDER;			
	
	@Before
	public void setUpBasePath(){
		PROJ_MAIN_FOLDER = PROJ_TESTS_FOLDER; 
		SOURCE_FOLDER = TESTS_FOLDER;
	}

	@After
	public void tearDownBasePath(){
		PROJ_MAIN_FOLDER = ORIGINAL_KOANS_PROJ_DIR;
		SOURCE_FOLDER = ORIGINAL_SRC_DIR;
	}
	
	@Test
	public void testThatKomparatorBombsWhenNotFound() throws Exception {
		Method m = new Object(){
			@SuppressWarnings("unused") @Koan public void someMethod(){}
		}.getClass().getDeclaredMethod("someMethod");
		KoanComparator comparator = new KoanComparator("meh");
		try{
			comparator.compare(new KoanMethod("2",m), new KoanMethod("1",m));
		}catch(RuntimeException fileNotFound){}
	}
	
	@Test
	public void testComparatorRanksByOrder() throws Exception {
		Class<? extends Object> clazz = new Object(){
			@SuppressWarnings("unused") @Koan public void someMethodOne(){}
			@SuppressWarnings("unused") @Koan public void someMethodTwo(){}
		}.getClass();
		KoanMethod m1 = new KoanMethod("",clazz.getDeclaredMethod("someMethodOne"));
		KoanMethod m2 = new KoanMethod("",clazz.getDeclaredMethod("someMethodTwo"));
		List<KoanMethod> methods = Arrays.asList(m2,m1);
		Collections.sort(methods, new KoanComparator("someMethodOne","someMethodTwo"));
		assertSame(m1,methods.get(0));
		assertSame(m2,methods.get(1));
	}
}


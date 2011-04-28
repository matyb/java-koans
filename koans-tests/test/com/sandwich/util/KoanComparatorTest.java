package com.sandwich.util;

import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;


public class KoanComparatorTest {

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
	
	@Test
	public void testComparatorRanksByOrder_compliment() throws Exception {
		Class<? extends Object> clazz = new Object(){
			@SuppressWarnings("unused") @Koan public void someMethodOne(){}
			@SuppressWarnings("unused") @Koan public void someMethodTwo(){}
		}.getClass();
		KoanMethod m1 = new KoanMethod("",clazz.getDeclaredMethod("someMethodOne"));
		KoanMethod m2 = new KoanMethod("",clazz.getDeclaredMethod("someMethodTwo"));
		List<KoanMethod> methods = Arrays.asList(m1,m2);
		Collections.sort(methods, new KoanComparator("someMethodTwo","someMethodOne"));
		assertSame(m2,methods.get(0));
		assertSame(m1,methods.get(1));
	}
}


package com.sandwich.util;

import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.path.CommandLineTestCase;

public class KoanComparatorTest extends CommandLineTestCase {
	
    interface Caps {
        public String capitalize(String name);
    }
    
	@Test
	public void testThatKomparatorBombsWhenNotFound() throws Exception {
		Method m = new Object(){
			@Koan public void someMethod(){}
		}.getClass().getDeclaredMethod("someMethod");
		KoanComparator comparator = new KoanComparator();
		try{
			comparator.compare(KoanMethod.getInstance("2",m), KoanMethod.getInstance("1",m));
		}catch(RuntimeException fileNotFound){}
	}
	
	@Test
	public void testComparatorRanksByOrder() throws Exception {
		Class<? extends Object> clazz = new Object(){
			@Koan public void someMethodOne(){}
			@Koan public void someMethodTwo(){}
		}.getClass();
		KoanMethod m1 = KoanMethod.getInstance("",clazz.getDeclaredMethod("someMethodOne"));
		KoanMethod m2 = KoanMethod.getInstance("",clazz.getDeclaredMethod("someMethodTwo"));
		List<KoanMethod> methods = Arrays.asList(m2,m1);
		Collections.sort(methods, new KoanComparator());
		assertSame(m1,methods.get(0));
		assertSame(m2,methods.get(1));
	}
	
    @Test
    public void testVariableNamesArentConfusedAsKoanMethodsWhenSorting() throws Exception {
        Class<? extends Object> clazz = new Object(){
            @SuppressWarnings("unused")
            String foo;
            @Koan public void bar(){}
            @Koan public void foo(){}
        }.getClass();
        KoanMethod m1 = KoanMethod.getInstance("", clazz.getDeclaredMethod("bar"));
        KoanMethod m2 = KoanMethod.getInstance("", clazz.getDeclaredMethod("foo"));
        List<KoanMethod> methods = Arrays.asList(m2,m1);
        Collections.sort(methods, new KoanComparator());
        assertSame(m1,methods.get(0));
        assertSame(m2,methods.get(1));
    }
}


package com.sandwich.koan.runner;

import java.lang.reflect.Method;

import com.sandwich.koan.KoanMethod;
import com.sandwich.util.Counter;

public class KoanMethodRunner {

	public static Throwable run(Object suite, KoanMethod koan, Counter successfull){
		try {
			Method method = koan.getMethod();
			method.setAccessible(true);
			method.invoke(suite);
			successfull.count();
		} catch (Throwable t) {
			return t;
		}
		return null;
	}
	
}

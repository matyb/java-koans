package com.sandwich.koan.runner;

import com.sandwich.koan.KoanMethod;
import com.sandwich.util.Counter;

public class KoanMethodRunner {

	public static Throwable run(Object suite, KoanMethod koan, Counter successfull){
		try {
			koan.getMethod().invoke(suite, (Object[]) null);
			successfull.count();
		} catch (Throwable t) {
			return t;
		}
		return null;
	}
	
}

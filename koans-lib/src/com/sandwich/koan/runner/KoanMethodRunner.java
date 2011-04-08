package com.sandwich.koan.runner;

import com.sandwich.koan.KoanMethod;
import com.sandwich.util.Counter;

public class KoanMethodRunner {

	public static Throwable run(Object suite, KoanMethod koan, Counter successfull){
		Throwable exception = null;
		try {
			koan.getMethod().invoke(suite, (Object[]) null);
			successfull.count();
		} catch (Throwable t) {
			while(t.getCause() != null){
				t = t.getCause();
			}
			exception = t;
		}
		return exception;
	}
	
}

package com.sandwich.util;

import com.sandwich.koan.KoanIncompleteException;
import com.sandwich.koan.constant.KoanConstants;

public class Assert {

	static final String EXPECTED	= "expected:<";
	static final String END 		= ">";
	static final String BUT_WAS 	= "> but was:<";
	
	public static void assertEquals(String msg, Object o0, Object o1){
		if(o0 == null && o1 != null){
			fail(msg, o0, o1);
		}
		if(o1 == null && o0 != null){
			fail(msg, o0, o1);
		}
		// not if o0 == o1 return, because equals may violate contract (though
		// that's obviously strongly discouraged), but cannot invoke equals on 
		// null pointer w/o sacrificing functionality from anticipating failure
		if(o1 == null && o0 == null){
			return;
		}
		if(!o0.equals(o1)){
			fail(msg, o0, o1);
		}
	}
	
	public static void assertEquals(Object o0, Object o1){
		assertEquals("", o0, o1);
	}

	public static void assertTrue(Object t){
		assertEquals(true,t);
	}
	
	public static void assertFalse(Object f){
		assertEquals(false,f);
	}
	
	public static void assertNull(Object o){
		assertEquals(null, o);
	}
	
	public static void assertNotNull(Object o){
		if(o == null){
			fail("something other than null",o);
		}
	}
	
	public static void assertSame(Object o0, Object o1){
		if(o0 != o1){
			fail("Are the same instance... ",o0,o1);
		}
	}
	
	public static void assertNotSame(Object o0, Object o1){
		if(o0 == o1){
			fail("Not the same instance... ",o0,o1);
		}
	}
	
	public static void fail(Object o0, Object o1) throws KoanIncompleteException {
		fail("", o0, o1);
	}
	
	public static void fail(String msg, Object o0, Object o1){
		fail(msg+(msg.length() == 0 ? "" : KoanConstants.EOL)+EXPECTED+o0+BUT_WAS+o1+END);
	}
	
	public static void fail(String msg){
		throw new KoanIncompleteException(msg);
	}
}

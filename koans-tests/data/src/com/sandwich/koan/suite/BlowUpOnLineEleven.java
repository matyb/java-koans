package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanIncompleteException;

public class BlowUpOnLineEleven {
	// gotta put it on line 11 thus the two spaces here
	// 
	@Koan
	public void blowUpOnLineEleven() {
		throw new KoanIncompleteException(null);
	}
	
}

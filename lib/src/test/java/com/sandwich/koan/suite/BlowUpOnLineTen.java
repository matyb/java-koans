package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanIncompleteException;

public class BlowUpOnLineTen {
	// gotta blow up on line 10 thus the comment
	@Koan
	public void blowUpOnLineTen(){
		throw new KoanIncompleteException(null);
	}
}

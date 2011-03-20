package com.sandwich.koan.suite;

import junit.framework.AssertionFailedError;

import com.sandwich.koan.Koan;

public class BlowUpOnLineEleven {

	@Koan(/*"i blow up on line eleven"*/)
	public void blowUpOnLineEleven() {
		throw new AssertionFailedError(null);
	}
	
}

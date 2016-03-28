package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;

public class BlowUpOnLineTen extends BlowUpOnLineEleven {
	// gotta blow up on line 10 thus the two spaces
	//
	@Koan
	public void blowUpOnLineTen(){
		super.blowUpOnLineEleven();
	}
}

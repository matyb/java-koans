package com.sandwich.koan.suite;

import com.sandwich.koan.Koan;

public class BlowUpOnLineTen extends BlowUpOnLineEleven {

	
	@Koan(/*"i blow up on line 10"*/)
	public void blowUpOnLineTen(){
		super.blowUpOnLineEleven();
	}
}

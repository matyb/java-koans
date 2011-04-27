package com.sandwich.koan.cmdline.behavior;

import com.sandwich.util.Strings;

public abstract class AbstractArgumentBehavior implements ArgumentBehavior{
	
	public String getErrorMessage() {
		return Strings.getMessage(getClass(), "error");
	}
	
	public String getSuccessMessage() {
		return Strings.getMessage(getClass(), "success");
	}
	
}

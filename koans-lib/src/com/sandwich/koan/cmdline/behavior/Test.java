package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.util.FileCompiler;

public class Test extends AbstractArgumentBehavior{

	public void run(String value) throws IOException {
		FileCompiler.compileRelative("koans-tests", "test", "bin");
	}
	
}

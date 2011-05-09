package com.sandwich.koan.cmdline.behavior;

import java.io.IOException;

import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.util.io.FileCompiler;

public class Test extends AbstractArgumentBehavior{

	public void run(String value) throws IOException {
		FileCompiler.compileRelative(KoanConstants.PROJ_TESTS_FOLDER, KoanConstants.TESTS_FOLDER, KoanConstants.BIN_FOLDER);
	}
	
}

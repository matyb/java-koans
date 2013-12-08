package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.EOLS;
import static com.sandwich.koan.constant.KoanConstants.EXPECTATION_LEFT_ARG;
import static com.sandwich.koan.constant.KoanConstants.EXPECTED_LEFT;
import static com.sandwich.koan.constant.KoanConstants.EXPECTED_RIGHT;
import static com.sandwich.koan.constant.KoanConstants.__;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import com.sandwich.koan.KoanIncompleteException;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.result.KoanMethodResult;
import com.sandwich.util.ExceptionUtils;
import com.sandwich.util.Strings;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.filecompiler.CompilerConfig;
import com.sandwich.util.io.filecompiler.FileCompiler;

public class KoanMethodRunner {

	private static final String EXPECTED_PROPERTY_KEY = "expected";
	
	public static KoanMethodResult run(Object suite, KoanMethod koan){
		try {
			Method method = koan.getMethod();
			method.setAccessible(true);
			method.invoke(suite);
		} catch (Throwable t) {
			Throwable tempException = t;
			String message = ExceptionUtils.convertToPopulatedStackTraceString(t);
			while(tempException != null){
				if(tempException instanceof KoanIncompleteException){
					t = (KoanIncompleteException)tempException;
					message = t.getMessage();
					if(message.contains(Strings.getMessage(EXPECTED_PROPERTY_KEY) + EXPECTED_LEFT + __ + EXPECTED_RIGHT)) {
						logExpectationOnWrongSideWarning(suite.getClass(), koan.getMethod());
					}
					break;
				}
				tempException = tempException.getCause();
			}
			return new KoanMethodResult(koan, message, getOriginalLineNumber(t, suite.getClass()));
		}
		return KoanMethodResult.PASSED;
	}
	
	private static void logExpectationOnWrongSideWarning(Class<?> firstFailingSuite, Method firstFailingMethod) {
		Logger.getLogger(CommandLineArgumentRunner.class.getSimpleName()).severe(
				new StringBuilder(
						firstFailingSuite.getSimpleName()).append(
						".").append(
						firstFailingMethod.getName()).append(
						" ").append(
						EXPECTATION_LEFT_ARG).toString());
	}
	
	/**
	 * Return the line number found closest to the point of failure, with a reference to the 
	 * failing suite's classname.
	 * 
	 * @param t
	 * @param failingSuite
	 * @return
	 */
	static String getOriginalLineNumber(Throwable t, Class<?> failingSuite){
		if(failingSuite != null &&
				CompilerConfig.getSuffix(FileCompiler.getSourceFileFromClass(DirectoryManager.getSourceDir(), failingSuite.getName()).getAbsolutePath())
					.equals(".java")){
			String[] lines = ExceptionUtils.convertToPopulatedStackTraceString(t).split(EOLS);
			for(int i = lines.length - 1; i >= 0; --i){
				String line = lines[i];
				if(line.contains(failingSuite.getName())){
					int start = line.indexOf(KoanConstants.LINE_NO_START)+KoanConstants.LINE_NO_START.length();
					int end = line.lastIndexOf(KoanConstants.LINE_NO_END);
					end = end > line.length() ? line.length() : end;
					return line.substring(start, end);
				}
			}
		}
		return null;
	}
	
}
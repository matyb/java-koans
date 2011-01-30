package com.sandwich.koan.runner;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import static com.sandwich.koan.KoanConstants.__;
import static com.sandwich.koan.KoanConstants.EXPECTATION_LEFT_ARG;
import static com.sandwich.koan.KoanConstants.EXPECTED_LEFT;
import static com.sandwich.koan.KoanConstants.EXPECTED_RIGHT;
import static com.sandwich.koan.KoanConstants.LINE_NO_START;
import static com.sandwich.koan.KoanConstants.LINE_NO_END;
import static com.sandwich.koan.KoanConstants.SUITE_PKG;
import static com.sandwich.koan.KoanConstants.EOLS;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoansResult;
import com.sandwich.koan.runner.ui.ConsolePresenter;
import com.sandwich.koan.runner.ui.SuitePresenter;

class KoanSuiteRunner {

	SuitePresenter presenter = null;
	
	public static void main(final String... args) throws Throwable {
		SuiteRunnerFactory.getSuiteRunner(args).run();
	}

	void run() throws ClassNotFoundException, IOException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Map<Object, List<Method>> koans = getKoans();
		KoansResult result = runKoans(koans);
		getPresenter().displayResult(result);
	}
	
	SuitePresenter getPresenter(){
		if(presenter == null){
			presenter = new ConsolePresenter();
		}
		return presenter;
	}
	
	Map<Object, List<Method>> getKoans() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Class<?>> koanSuites = PathToEnlightment.getPathToEnlightment();
		Map<Object, List<Method>> koans = new LinkedHashMap<Object, List<Method>>();
		for(Class<?> koanSuite : koanSuites){
			List<Method> koanMethods = new ArrayList<Method>();
			for(Method koan : koanSuite.getMethods()){
				if(koan.getAnnotation(Koan.class) != null){
					koanMethods.add(koan);
				}
			}
			koans.put(koanSuite.newInstance(), koanMethods);
		}
		return koans;
	}

	int getAllValuesSize(Map<Object, List<Method>> koans) {
		int size = 0;
		for (Entry<Object, List<Method>> entry : koans.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}

	KoansResult runKoans(Map<Object, List<Method>> koans)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		int successfull = 0;
		int totalKoanMethods = getAllValuesSize(koans);
		List<Class<?>> passingSuites = new ArrayList<Class<?>>();
		List<Class<?>> failingSuites = new ArrayList<Class<?>>();
		Throwable failure = null;
		Class<?> firstFailingSuite = null;
		Method firstFailingMethod = null;
		for (Entry<Object, List<Method>> e : koans.entrySet()) {
			final Object suite = e.getKey();
			final List<Method> methods = e.getValue();
			boolean testsPassed = true;
			for (final Method koan : methods) {
				try {
					koan.invoke(suite, (Object[]) null);
					successfull++;
				} catch (Throwable t) {
					testsPassed = false;
					while(t.getCause() != null){
						t = t.getCause();
					}
					if (failure == null) {
						failure = t;
						firstFailingSuite = suite.getClass();
						firstFailingMethod = koan;
					}
				}
			}
			if(testsPassed){
				passingSuites.add(suite.getClass());
			}else{
				failingSuites.add(suite.getClass());
			}
		}
		String message = failure instanceof AssertionError ? failure.getMessage() : null;
		if(message != null && message.contains(EXPECTED_LEFT+__+EXPECTED_RIGHT)){
			logExpectationOnWrongSideWarning(firstFailingSuite, firstFailingMethod);
		}
		return new KoansResult(successfull, totalKoanMethods, firstFailingSuite, firstFailingMethod, 
				message, getLineNumber(failure), passingSuites, failingSuites);
	}

	private void logExpectationOnWrongSideWarning(Class<?> firstFailingSuite,
			Method firstFailingMethod) {
		Logger.getLogger(KoanSuiteRunner.class.getSimpleName()).severe(
				new StringBuilder(
						firstFailingSuite.getSimpleName()).append(
						".").append(
						firstFailingMethod.getName()).append(
						" ").append(
						EXPECTATION_LEFT_ARG).toString());
	}

	private String getLineNumber(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		if(t == null){
			return null;
		}
		t.printStackTrace(new PrintWriter(stringWriter));
		String[] lines = stringWriter.toString().split(EOLS);
		for(int i = lines.length - 1; i >= 0; --i){
			String line = lines[i];
			if(line.contains(SUITE_PKG)){
				return line.substring(
						line.indexOf(LINE_NO_START)+LINE_NO_START.length(),
						line.lastIndexOf(LINE_NO_END));
			}
		}
		return null;
	}
}

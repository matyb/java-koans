package com.sandwich.koan.runner;

import static com.sandwich.koan.KoanConstants.EOLS;
import static com.sandwich.koan.KoanConstants.EXPECTATION_LEFT_ARG;
import static com.sandwich.koan.KoanConstants.EXPECTED_LEFT;
import static com.sandwich.koan.KoanConstants.EXPECTED_RIGHT;
import static com.sandwich.koan.KoanConstants.__;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.sandwich.koan.KoanConstants;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.KoansResult;
import com.sandwich.koan.runner.PathToEnlightenment.Path;
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
		KoansResult result = runKoans();
		getPresenter().displayResult(result);
	}
	
	SuitePresenter getPresenter(){
		if(presenter == null){
			presenter = new ConsolePresenter();
		}
		return presenter;
	}

	int getAllValuesSize(Map<Object, List<Method>> koans) {
		int size = 0;
		for (Entry<Object, List<Method>> entry : koans.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}

	KoansResult runKoans()
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Path path = getPathToEnlightenment();
		int successfull = 0;
		List<Class<?>> passingSuites = new ArrayList<Class<?>>();
		List<Class<?>> failingSuites = new ArrayList<Class<?>>();
		Throwable failure = null;
		Class<?> firstFailingSuite = null;
		Method firstFailingMethod = null;
		for(Entry<String, Map<Object, List<KoanMethod>>> packages : path){
			for (Entry<Object, List<KoanMethod>> e : packages.getValue().entrySet()) {
				final Object suite = e.getKey();
				final List<KoanMethod> methods = e.getValue();
				boolean testsPassed = true;
				for (final KoanMethod koan : methods) {
					try {
						koan.getMethod().invoke(suite, (Object[]) null);
						successfull++;
					} catch (Throwable t) {
						testsPassed = false;
						while(t.getCause() != null){
							t = t.getCause();
						}
						if (failure == null) {
							failure = t;
							firstFailingSuite = suite.getClass();
							firstFailingMethod = koan.getMethod();
						}
					}
				}
				if(testsPassed){
					passingSuites.add(suite.getClass());
				}else{
					failingSuites.add(suite.getClass());
				}
			}
		}
		String message = failure instanceof AssertionError ? failure.getMessage() : null;
		if(message != null && message.contains(EXPECTED_LEFT+__+EXPECTED_RIGHT)){
			logExpectationOnWrongSideWarning(firstFailingSuite, firstFailingMethod);
		}
		return new KoansResult(successfull, path.getTotalNumberOfKoans(), firstFailingSuite, firstFailingMethod, 
				message, getLineNumber(failure, firstFailingSuite), passingSuites, failingSuites);
	}

	/**
	 * permit forwarding by overriding classes
	 * @return
	 */
	protected Path getPathToEnlightenment() {
		return PathToEnlightenment.getPathToEnlightment();
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

	private String getLineNumber(Throwable t, Class<?> failingCase) {
		StringWriter stringWriter = new StringWriter();
		if(t == null){
			return null;
		}
		t.printStackTrace(new PrintWriter(stringWriter));
		String[] lines = stringWriter.toString().split(EOLS);
		for(int i = lines.length - 1; i >= 0; --i){
			String line = lines[i];
			if(line.contains(failingCase.getName())){
				return line.substring(
						line.indexOf(KoanConstants.LINE_NO_START)+KoanConstants.LINE_NO_START.length(),
						line.lastIndexOf(KoanConstants.LINE_NO_END));
			}
		}
		return null;
	}
}

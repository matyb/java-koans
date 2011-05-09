package com.sandwich.koan.runner;

import static com.sandwich.koan.constant.KoanConstants.EOLS;
import static com.sandwich.koan.constant.KoanConstants.EXPECTATION_LEFT_ARG;
import static com.sandwich.koan.constant.KoanConstants.EXPECTED_LEFT;
import static com.sandwich.koan.constant.KoanConstants.EXPECTED_RIGHT;
import static com.sandwich.koan.constant.KoanConstants.__;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.sandwich.koan.KoanIncompleteException;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.KoanSuiteResult;
import com.sandwich.koan.KoanSuiteResult.KoanResultBuilder;
import com.sandwich.koan.cmdline.behavior.AbstractArgumentBehavior;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.koan.path.PathToEnlightenment.Path;
import com.sandwich.koan.ui.ConsolePresenter;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.util.Counter;

public class RunKoans extends AbstractArgumentBehavior {

	private SuitePresenter presenter;
	private Path pathToEnlightenment;
	
	public RunKoans(){
		this(null, null); // use defaults coded in getters
	}
	
	public RunKoans(SuitePresenter presenter, Path pathToEnlightenment){
		this.presenter = presenter;
		this.pathToEnlightenment = pathToEnlightenment;
	}
	
	public void run(String value) throws Exception {
		getPresenter().displayResult(runKoans());
	}

	KoanSuiteResult runKoans() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Counter successfull = new Counter();
		List<Class<?>> passingSuites = new ArrayList<Class<?>>();
		List<Class<?>> failingSuites = new ArrayList<Class<?>>();
		Throwable failure = null;
		Class<?> firstFailingSuite = null;
		KoanMethod firstFailingMethod = null;
		boolean testsPassed = true;
		String level = null;
		for (Entry<String, Map<Object, List<KoanMethod>>> packages : getPathToEnlightement()) {
			for (Entry<Object, List<KoanMethod>> e : packages.getValue()
					.entrySet()) {
				final Object suite = e.getKey();
				if(testsPassed){
					final List<KoanMethod> methods = e.getValue();
					for (final KoanMethod koan : methods) {
						failure = KoanMethodRunner.run(suite, koan,
								successfull);
						Throwable tempException = failure;
						while(tempException != null){
							if(tempException instanceof KoanIncompleteException){
								failure = (KoanIncompleteException)tempException;
								break;
							}
							tempException = tempException.getCause();
						}
						if (failure != null) {
							testsPassed = false;
							firstFailingSuite = suite.getClass();
							firstFailingMethod = koan;
							level = packages.getKey();
							break; // might as well not run the rest
						}
					}
					if (testsPassed) {
						passingSuites.add(suite.getClass());
					} else {
						failingSuites.add(suite.getClass());
					}
				}else{
					failingSuites.add(suite.getClass());
				}
			}
		}
		// only display message if it is from a KoanIncompleteException
		String message = failure != null
				&& KoanIncompleteException.class.isAssignableFrom(failure.getClass()) ? 
						failure.getMessage() : formatNormalException(failure);
		if (failure instanceof KoanIncompleteException && message.contains(EXPECTED_LEFT + __ + EXPECTED_RIGHT)) {
			logExpectationOnWrongSideWarning(firstFailingSuite,
					firstFailingMethod.getMethod());
		}
		return new KoanResultBuilder()	.level(level)
										.numberPassing((int)successfull.getCount())
										.totalNumberOfKoanMethods(getPathToEnlightement().getTotalNumberOfKoans())
										.failingCase(firstFailingSuite)
										.failingMethod(firstFailingMethod).message(message)
										.lineNumber(getOriginalLineNumber(failure, firstFailingSuite))
										.passingCases(passingSuites).remainingCases(failingSuites).build();
	}
	
	private String formatNormalException(Throwable failure) {
		return failure == null ? "" : fillStackTrace(failure).toString();
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
		StringWriter stringWriter = fillStackTrace(t);
		String[] lines = stringWriter.toString().split(EOLS);
		if(failingSuite != null){
			for(int i = lines.length - 1; i >= 0; --i){
				String line = lines[i];
				if(line.contains(failingSuite.getName())){
					return line.substring(
							line.indexOf(KoanConstants.LINE_NO_START)+KoanConstants.LINE_NO_START.length(),
							line.lastIndexOf(KoanConstants.LINE_NO_END));
				}
			}
		}
		return null;
	}

	private static StringWriter fillStackTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		if(t == null){
			return stringWriter;
		}
		t.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter;
	}

	private Path getPathToEnlightement() {
		if(pathToEnlightenment == null){
			pathToEnlightenment = PathToEnlightenment.getPathToEnlightment();
		}
		return pathToEnlightenment;
	}

	private SuitePresenter getPresenter(){
		if(presenter == null){
			presenter = new ConsolePresenter();
		}
		return presenter;
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
}

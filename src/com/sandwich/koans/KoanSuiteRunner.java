package com.sandwich.koans;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koans.suites.AllKoans;

class KoanSuiteRunner {

	public static void main(String... args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, IOException, InstantiationException {
		new KoanSuiteRunner().run();
	}

	void run() throws ClassNotFoundException, IOException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Map<Object, List<Method>> koans = getKoans();
		KoansResult result = runKoans(koans);
		printResult(koans, result);
	}
	
	private Map<Object, List<Method>> getKoans() throws InstantiationException, IllegalAccessException {
		List<Class<?>> koanSuites = AllKoans.getKoans();
		Map<Object, List<Method>> koans = new LinkedHashMap<Object, List<Method>>();
		for(Class<?> koanSuite : koanSuites){
			List<Method> koanMethods = new ArrayList<Method>();
			for(Method koan : koanSuite.getMethods()){
				if(koan.getAnnotation(Koan.class) != null){
					koanMethods.add(koan);
				}
			}
			Collections.sort(koanMethods, new KoanMethodComparater());
			koans.put(koanSuite.newInstance(), koanMethods);
		}
		return koans;
	}

	void printResult(Map<Object, List<Method>> koans,
			KoansResult result) {
		System.out.println("***********************");
		System.out.println("* Java TDD Koans v.01 *");
		System.out.println("***********************\n");
		printChart(koans, result);
		if (result.isAllKoansSuccessful()) {
			System.out
					.println("Way to go! You've succeeded where "
							+ "many have failed. Stand proud, get a tattoo or something.");
		} else {
			String message = result.getMessage();
			System.out.println(message == null || message.length() == 0 ? ""
					: '\n' + message + '\n');
			System.out.println("Out of " + getAllValuesSize(koans)
					+ " you have conquered " + result.getNumberPassing()
					+ " koan" + (result.getNumberPassing() != 1 ? 's' : "")
					+ "! Keep going, you will persevere!\n");
			printSuggestion(result);
		}
	}

	void printSuggestion(KoansResult result) {
		System.out.println("Ponder what's going wrong in the "
				+ result.getFailingCase().getName() + " class's "
				+ result.getFailingMethod().getName() + " method.");
	}

	void printChart(Map<Object, List<Method>> koans,
			KoansResult result) {
		StringBuilder sb = new StringBuilder("Progress\n");
		sb.append('[');
		int allValuesSize = getAllValuesSize(koans);
		int numberPassing = result.getNumberPassing();
		double percentPassing = ((double) numberPassing)
				/ ((double) allValuesSize);
		int fifty = 50;
		int percentWeightedToFifty = (int) (percentPassing * fifty);
		for (int i = 0; i < fifty; i++) {
			if (i < percentWeightedToFifty) {
				sb.append('X');
			} else {
				sb.append('-');
			}
		}
		sb.append(']');
		sb.append(' ');
		// TODO make this less hacky! store total number in result perhaps?
		numberPassing = numberPassing == -1 ? allValuesSize : numberPassing;
		sb.append(numberPassing + "/" + allValuesSize);
		System.out.println(sb.toString());
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
		KoansResult result = null;
		for (Entry<Object, List<Method>> e : koans.entrySet()) {
			final Object suite = e.getKey();
			final List<Method> methods = e.getValue();
			for (final Method koan : methods) {
				try {
					koan.invoke(suite, (Object[]) null);
					successfull++;
				} catch (Throwable t) {
					if (t instanceof InvocationTargetException) {
						t = t.getCause();
					}
					if (t instanceof AssertionError) {
						if (result == null) {
							result = new KoansResult(successfull,
									suite.getClass(), koan, t.getMessage());
						}
					}
					if (result == null) {
						result = new KoansResult(successfull, suite.getClass(),
								koan);
					}
				}
			}
		}
		if (result == null) {
			result = new KoansResult();
		} else {
			result.numberPassing = successfull;
		}
		return result == null ? // all koans passed!
		new KoansResult()
				: result;
	}
}

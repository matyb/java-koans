package com.sandwich.koans;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
			koans.put(koanSuite.newInstance(), koanMethods);
		}
		return koans;
	}

	void printResult(Map<Object, List<Method>> koans,
			KoansResult result) {
		System.out.println("***********************");
		System.out.println("*   Java Koans v.02   *");
		System.out.println("***********************\n");
		printPassingFailing(result);
		printChart(koans, result);
		if (result.isAllKoansSuccessful()) {
			System.out.println("\nWay to go! You've completed all of the koans! Feel like writing any?");
		} else {
			String message = result.getMessage();
			System.out.println(message == null || message.length() == 0 ? 
					"" : '\n' + "What went wrong:\n" + message + '\n');
			printSuggestion(result);
			int totalKoans = result.getTotalNumberOfKoans();
			int numberPassing = result.getNumberPassing();
			System.out.println("You have conquered " + numberPassing
					+ " out of " + totalKoans
					+ " koan" + (totalKoans != 1 ? 's' : "")
					+ "! Keep going, you will persevere!\n");
		}
	}

	private void printPassingFailing(KoansResult result) {
		StringBuilder sb = new StringBuilder();
		appendLabeledClassesList("Passing Suites: ", result.getPassingSuites(), sb);
		appendLabeledClassesList("Remaining Suites: ", result.getRemainingSuites(), sb);
		System.out.println(sb.toString());
	}

	private void appendLabeledClassesList(String suiteStatus, List<Class<?>> suites, StringBuilder sb) {
		if(suites == null || suites.isEmpty()){
			return;
		}
		sb.append(suiteStatus);
		for(Class<?> c : suites){
			sb.append(c.getSimpleName());
			if(suites.indexOf(c) != suites.size() - 1){
				sb.append(", ");
			}
		}
		sb.append('\n');
	}

	void printSuggestion(KoansResult result) {
		Method failedKoan = result.getFailingMethod();
		Koan annotation = failedKoan.getAnnotation(Koan.class);
		if(annotation != null){
			System.out.println(annotation.value()+'\n');
		}
		System.out.println("Ponder what's going wrong in the "
				+ result.getFailingCase().getSimpleName() + " class's "
				+ result.getFailingMethod().getName() + " method.\n");
	}

	void printChart(Map<Object, List<Method>> koans,
			KoansResult result) {
		StringBuilder sb = new StringBuilder("Progress:\n");
		sb.append('[');
		int numberPassing = result.getNumberPassing();
		int totalKoans = result.getTotalNumberOfKoans();
		double percentPassing = ((double) numberPassing)
				/ ((double) totalKoans);
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
		numberPassing = numberPassing == -1 ? result.getNumberPassing() : numberPassing;
		sb.append(numberPassing + "/" + totalKoans);
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
		int totalKoanMethods = getAllValuesSize(koans);
		List<Class<?>> passingSuites = new ArrayList<Class<?>>();
		List<Class<?>> failingSuites = new ArrayList<Class<?>>();
		KoansResult result = null;
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
					if (t instanceof InvocationTargetException) {
						t = t.getCause();
					}
					if (t instanceof AssertionError) {
						if (result == null) {
							result = new KoansResult(successfull, totalKoanMethods, suite.getClass(), koan, t.getMessage());
						}
					}
					if (result == null) {
						result = new KoansResult(successfull, totalKoanMethods, suite.getClass(), koan);
					}
				}
			}
			if(testsPassed){
				passingSuites.add(suite.getClass());
			}else{
				failingSuites.add(suite.getClass());
			}
		}
		if (result == null) {
			result = new KoansResult(successfull, totalKoanMethods);
		} else {
			result.numberPassing = successfull;
		}
		result.passingCases = passingSuites;
		result.remainingCases = failingSuites;
		return result;
	}
}

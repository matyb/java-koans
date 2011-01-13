package com.sandwich.koans;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class KoanSuiteRunner {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, IOException, InstantiationException {
		Map<Object, List<Method>> koans = getKoans();
		KoansResult result = runKoans(koans);
		printResult(koans, result);
	}

	private static void printResult(Map<Object, List<Method>> koans,
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

	private static void printSuggestion(KoansResult result) {
		System.out.println("Ponder what's going wrong in the "
				+ result.getFailingCase().getName() + " class's "
				+ result.getFailingMethod().getName() + " method.");
	}

	private static void printChart(Map<Object, List<Method>> koans,
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

	private static int getAllValuesSize(Map<Object, List<Method>> koans) {
		int size = 0;
		for (Entry<Object, List<Method>> entry : koans.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}

	private static KoansResult runKoans(Map<Object, List<Method>> koans)
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

	private static Map<Object, List<Method>> getKoans()
			throws ClassNotFoundException, IOException, InstantiationException,
			IllegalAccessException {
		List<Class<?>> koanClasses = findKoanClasses(KoanSuiteRunner.class
				.getPackage().getName() + ".suites");
		Map<Object, List<Method>> koans = new LinkedHashMap<Object, List<Method>>();
		for (Class<?> clazz : koanClasses) {
			Object suite = (Object) clazz.newInstance();
			ArrayList<Method> methods = new ArrayList<Method>();
			koans.put(suite, methods);
			for (Method m : clazz.getMethods()) {
				Koan annotation = m.getAnnotation(Koan.class);
				if (annotation != null) {
					methods.add(m);
				}
			}
		}
		return koans;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static List<Class<?>> findKoanClasses(
			String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findKoanClasses(directory, packageName));
		}
		Collections.sort(classes, new KoanSuiteComparater());
		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findKoanClasses(
			File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findKoanClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class<?> c = Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6));
				if (c.getAnnotation(KoanOrder.class) != null) {
					classes.add((Class<?>) c);
				}else{
					Logger.getAnonymousLogger()
							.info(c + " is in the suites pkg, however it lacks a "
									+ KoanOrder.class.getSimpleName()
									+ " annotation.\nThis was probably not intentional.");
				}
			}
		}
		return classes;
	}

	private static class KoanSuiteComparater implements Comparator<Class<?>> {
		@Override
		public int compare(Class<?> o0, Class<?> o1) {
			KoanOrder order0 = o0.getAnnotation(KoanOrder.class);
			KoanOrder order1 = o1.getAnnotation(KoanOrder.class);
			int comparison = 0;
			if (order0 == null && order1 != null) {
				comparison = 1;
			}
			else if (order1 == null && order0 != null) {
				comparison = -1;
			}
			else if(order0 != null && order1 != null){
				comparison = Integer.valueOf(order0.order()).compareTo(order1.order());
			}
			if(comparison == 0){
				Logger.getAnonymousLogger().info(
						o0.getSimpleName() + " and " + o1.getSimpleName() +
						" evaluate to the same " + KoanOrder.class.getSimpleName() + '.' +
						"\nThis will break the order the koans are evaluated and presented " +
						"to users.");
			}
			return comparison;
		}
	}

}

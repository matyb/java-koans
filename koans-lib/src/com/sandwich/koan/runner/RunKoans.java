package com.sandwich.koan.runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.cmdline.behavior.AbstractArgumentBehavior;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.koan.path.PathToEnlightenment.Path;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.result.KoanMethodResult;
import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.koan.result.KoanSuiteResult.KoanResultBuilder;
import com.sandwich.koan.ui.ConsolePresenter;
import com.sandwich.koan.ui.SuitePresenter;
import com.sandwich.util.Counter;
import com.sandwich.util.KoanComparator;
import com.sandwich.util.io.DynamicClassLoader;

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
	
	public void run(String value) {
		getPresenter().displayResult(runKoans());
	}

	KoanSuiteResult runKoans() {
		Counter successfull = new Counter();
		List<String> passingSuites = new ArrayList<String>();
		List<String> failingSuites = new ArrayList<String>();
		String level = null;
		KoanMethodResult failure = null;
		DynamicClassLoader loader = new DynamicClassLoader();
		Path pathToEnlightement = getPathToEnlightement();
		for (Entry<String, Map<String, Map<String, KoanElementAttributes>>> packages : pathToEnlightement) {
			for (Entry<String, Map<String, KoanElementAttributes>> e : packages.getValue().entrySet()) {
				String name = e.getKey().substring(e.getKey().lastIndexOf('.')+1);
				if(failure == null){
					Object suite = constructSuite(loader, e.getKey());
					final List<KoanElementAttributes> attributes = new ArrayList<KoanElementAttributes>(e.getValue().values());
					final List<KoanMethod> methods = mergeJavaFilesMethodsAndThoseInXml(suite, attributes, pathToEnlightement.getOnlyMethodNameToRun());
					Collections.sort(methods, new KoanComparator());
					for (final KoanMethod koan : methods) {
						KoanMethodResult result = KoanMethodRunner.run(suite, koan);
						if(KoanMethodResult.PASSED != result){
							// Test failed!
							failure = result;
							failingSuites.add(name);
							break;
						}else{
							successfull.count();
						}
					}
					if (failure == null) {
						passingSuites.add(name);
					}
					level = packages.getKey();
				}else{
					failingSuites.add(name);
				}
			}
		}
		return new KoanResultBuilder()	.level(level)
										.numberPassing((int)successfull.getCount())
										.totalNumberOfKoanMethods(pathToEnlightement.getTotalNumberOfKoans())
										.methodResult(failure)
										.passingCases(passingSuites).remainingCases(failingSuites).build();
	}

	private List<KoanMethod> mergeJavaFilesMethodsAndThoseInXml(Object suite,
			final List<KoanElementAttributes> attributes, String onlyMethodNameToRun) {
		final List<KoanMethod> methods = new ArrayList<KoanMethod>();
		final List<String> methodNames = new ArrayList<String>();
		for(KoanElementAttributes attributeSet : attributes){
			if(isMethodEligibleForRunning(onlyMethodNameToRun, attributeSet.name)){
				KoanMethod method = KoanMethod.getInstance(attributeSet);
				methods.add(method);
				methodNames.add(method.getMethod().getName());
			}
		}
		for(Method method : suite.getClass().getMethods()){
			if(!methodNames.contains(method.getName()) &&
					isMethodEligibleForRunning(onlyMethodNameToRun, method.getName()) &&
					method.getAnnotation(Koan.class) != null){
				methods.add(KoanMethod.getInstance(method));
			}
		}
		return methods;
	}

	private boolean isMethodEligibleForRunning(String onlyMethodNameToRun, String name) {
		return onlyMethodNameToRun == null || onlyMethodNameToRun.equals(name);
	}

	private Object constructSuite(DynamicClassLoader loader, String className) {
		Object suite;
		try {
			Class<?> clazz = loader.loadClass(className);
			if(!clazz.isAnonymousClass()){
				suite = clazz.newInstance();
			}else{
				Class<?> enclosingClass = clazz.getEnclosingClass();
				Constructor<?> cons = null;
				for(Constructor<?> c : clazz.getDeclaredConstructors()){
					for(Class<?> p : c.getParameterTypes()){
						if(p == enclosingClass){
							cons = c;
							break;
						}
					}
				}
				cons.setAccessible(true);
				suite = cons.newInstance(enclosingClass.newInstance());
			}
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		return suite;
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
}

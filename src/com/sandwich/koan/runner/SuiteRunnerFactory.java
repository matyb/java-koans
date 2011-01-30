package com.sandwich.koan.runner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanConstants;

public class SuiteRunnerFactory {

	private SuiteRunnerFactory() {}

	public static KoanSuiteRunner getSuiteRunner(final String[] args) {
		if(args.length > 0){
			return new SingleKoanRunner(args);
		}
		return new KoanSuiteRunner();
	}
	
	static class SingleKoanRunner extends KoanSuiteRunner {
		private final String koanSuite;
		private final String koanName;

		private SingleKoanRunner(String...args) {
			this.koanSuite 	= args[0];
			this.koanName 	= args.length > 1 ? args[1] : null;
		}

		@Override public Map<Object, List<Method>> getKoans() 
				throws InstantiationException, IllegalAccessException, ClassNotFoundException{
			// for single suite
			stagePathToEnlightenment();
			
			// use parent implementation to nab the koans (preemtively screened
			// for suites matching koanSuite via staging)
			Map<Object, List<Method>> koans = super.getKoans();
			
			// if necessary, remove methods not specified by koanName
			if(koanName != null){
				stripAllByTheNamedKoan(koans);
			}
			return koans;
		}

		private void stripAllByTheNamedKoan(Map<Object, List<Method>> koans) {
			if(koans.size() != 1){
				Logger.getAnonymousLogger().warning("not just one koansuite remains, " +
						"check koan suite name argument - not likely that filtering by method will work.");
			}
			Collection<List<Method>> values = koans.values();
			for(List<Method> methods : values){
				Iterator<Method> koansListsIter = methods.iterator();
				while(koansListsIter.hasNext()){
					if(!koanName.equalsIgnoreCase(koansListsIter.next().getName())){
						koansListsIter.remove();
					}
				}
			}
		}

		private void stagePathToEnlightenment() throws ClassNotFoundException {
			Class<?> koanClass;
			if(koanSuite.contains(Koan.class.getPackage().getName())){
				koanClass = Class.forName(koanSuite);
			}else{
				koanClass = Class.forName(
						new StringBuilder(KoanConstants.SUITE_PKG).append('.').append(koanSuite).toString());
			}
			PathToEnlightment.koans = Arrays.asList(new Class<?>[]{ koanClass });
		}
	}
}

package com.sandwich.koan.runner;

import com.sandwich.koan.runner.PathToEnlightenment.Path;

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

		@Override public Path getPathToEnlightenment(){
			// for single suite
			PathToEnlightenment.stagePathToEnlightenment(koanSuite);
			
			// use parent implementation to nab the koans (preemptively screened
			// for suites matching koanSuite via staging)
			Path koans = super.getPathToEnlightenment();
			
			// if necessary, remove methods not specified by koanName
			if(koanName != null){
				PathToEnlightenment.removeAllKoanMethodsExcept(koans, koanName);
			}
			return koans;
		}
	}
}

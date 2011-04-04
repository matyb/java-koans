package com.sandwich.koan.runner;

import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;

public class AppLauncher implements Runnable {

	private final String[] args;
	
	public AppLauncher(String...args){
		this.args = args;
	}
	
	public static void main(final String... args) throws Throwable {
		new AppLauncher(args).run();
	}

	public void run() {
		try{
			new KoanSuiteRunner(
				new CommandLineArgumentBuilder(args).build()	
			).run();
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
}

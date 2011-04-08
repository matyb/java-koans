package com.sandwich.koan.runner;

import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.KoanConstants;

public class AppLauncher implements Runnable {

	private final String[] args;
	
	public AppLauncher(String...args){
		this.args = args;
	}
	
	public static void main(final String... args) throws Throwable {
		new AppLauncher(args).run();
		if(KoanConstants.DEBUG){
			StringBuilder argsBuilder = new StringBuilder();
			int argNumber = 0;
			for(String arg : args){
				argsBuilder.append("Argument number "+String.valueOf(++argNumber)+": '"+arg+"'");
			}
			System.out.println(argsBuilder.toString());
		}
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

package com.sandwich.koans.suites;

import com.sandwich.koans.Koan;
import com.sandwich.koans.KoanSuite;

public class AboutMocks  extends KoanSuite{
	
	/*
	 * Following two classes are used to demonstrate mocking
	 */
	static class ClassUnderTest {
		Collaborator c;
		ClassUnderTest(Collaborator c){
			this.c = c;
		}
		public boolean doSomething(){
			return c.doBusinessStuff();
		}
	}
	
	static interface Collaborator {
		public boolean doBusinessStuff();
	}
	
	@Koan("how can this pass without changing to assertTrue?")
	public void simpleAnonymousMock(){
		assertFalse(new ClassUnderTest(new Collaborator(){
			@Override
			public boolean doBusinessStuff() {
				return true; // <- hint 
			}
		}).doSomething());
		// TODO: ponder why this assertion was failing
		// originally
	}
	
	//TODO: perhaps show off some mocking frameworks?
	
}

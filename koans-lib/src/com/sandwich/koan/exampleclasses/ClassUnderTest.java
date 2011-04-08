package com.sandwich.koan.exampleclasses;


public class ClassUnderTest {
	Collaborator c;
	public ClassUnderTest(){
		// default is to pass a broken Collaborator, test should pass one
		// that doesn't throw exception
		this(new Collaborator(){
			public void doBusinessStuff() {
				throw new AssertionError("Default collaborator's behavior is complicating testing.");
			}
		});
	}
	public ClassUnderTest(Collaborator c){
		this.c = c;
	}
	public boolean doSomething(){
		c.doBusinessStuff();
		return true;
	}
}

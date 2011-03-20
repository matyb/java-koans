package advanced;

import com.sandwich.koan.Koan;

public class AboutMocks {
	
	/*
	 * Following two classes are used to demonstrate mocking
	 */
	static class ClassUnderTest {
		Collaborator c;
		ClassUnderTest(){
			// default is to pass a broken Collaborator, test should pass one
			// that doesn't throw exception
			this(new Collaborator(){
				@Override
				public void doBusinessStuff() {
					throw new AssertionError("Default collaborator's behavior is complicating testing.");
				}
			});
		}
		ClassUnderTest(Collaborator c){
			this.c = c;
		}
		public void doSomething(){
			c.doBusinessStuff();
		}
	}
	
	static interface Collaborator {
		public void doBusinessStuff();
	}
	
	@Koan("How can this pass without touching the ClassUnderTest?")
	public void simpleAnonymousMock(){
		// HINT: pass different Collaborator implementation to constructor
		// new ClassUnderTest(new Colloborator(){...
		new ClassUnderTest().doSomething();
		// TODO: ponder why this assertion was failing
		// originally... look in default constructor
	}
	
	//TODO: perhaps show off some mocking frameworks?
	
}

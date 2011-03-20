package advanced;

import com.sandwich.koan.Koan;

public class AboutMocks {
	
	@Koan()
	public void simpleAnonymousMock(){
		// HINT: pass different Collaborator implementation to constructor
		// new ClassUnderTest(new Collaborator(){...
		new ClassUnderTest().doSomething();
	}
	
	//TODO: perhaps show off some mocking frameworks?
	
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
	
}

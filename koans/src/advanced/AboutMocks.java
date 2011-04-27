package advanced;

import com.sandwich.koan.Koan;

public class AboutMocks {
	
	@Koan()
	public void simpleAnonymousMock(){
		// HINT: pass different Collaborator implementation to constructor
		// new ClassUnderTest(new Colloborator(){...
		new ClassUnderTest().doSomething();
		// TODO: ponder why this assertion was failing
		// originally... look in default constructor
	}
	
	static interface Collaborator {
		public void doBusinessStuff();
	}
	
	static class ClassUnderTest {
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

	
	//TODO: perhaps show off some mocking frameworks?
	
}

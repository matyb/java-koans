package advanced;

import com.sandwich.koan.Koan;
import com.sandwich.koan.exampleclasses.ClassUnderTest;

public class AboutMocks {
	
	@Koan()
	public void simpleAnonymousMock(){
		// HINT: pass different Collaborator implementation to constructor
		// new ClassUnderTest(new Colloborator(){...
		new ClassUnderTest().doSomething();
		// TODO: ponder why this assertion was failing
		// originally... look in default constructor
	}
	
	//TODO: perhaps show off some mocking frameworks?
	
}

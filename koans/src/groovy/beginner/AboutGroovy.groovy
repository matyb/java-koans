package groovy.beginner;

import com.sandwich.koan.Koan;
import static com.sandwich.util.Assert.*;
import static com.sandwich.koan.constant.KoanConstants.__; 

class AboutGroovy {

	@Koan 
	public void installAndConfigureGroovyAndFindThisFile(){
		fail("Looks like you got Groovy installed, delete this line to move on.");
	}
	
	@Koan groovyVoidAndPublicCanBeImplied(){
		fail("This wouldn't compile or be public in Java. Just delete or comment this line out.");
	}
	
	@Koan groovySemicolonsOption(){
		fail("This wouldn't compile in Java because there's no semicolon.")
	}
	
	@Koan groovyListLiteral(){
		def list = __
		assertEquals([1, 2, 3], list)
	}
	
	@Koan groovyListLiteralIsAnArrayList(){
		def arrayListClass = __
		assertEquals([].getClass(), arrayListClass)
	}
	
}
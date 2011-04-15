package beginner;

import java.util.ArrayList;
import java.util.Collection;

import com.sandwich.koan.Koan;
import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutInheritance {

	class Parent {
		public String doStuff() { return "parent"; }
	}
	class Child extends Parent {
		public String doStuff() { return "child"; }
		public String doStuff(String s) { return s; }
	}
	
	@Koan
	public void differenceBetweenOverloadingAndOverriding() {
		assertEquals(new Parent().doStuff(),__);
		assertEquals(new Child().doStuff(),__);
		assertEquals(new Child().doStuff("oh no"),__);
	}
	
	abstract class ParentTwo {
		abstract public Collection doStuff();
	}
	
	class ChildTwo extends ParentTwo {
		public Collection doStuff() { return null; };
	}
	
	@Koan
	public void overridenMethodsMayReturnSubtype() {
		// What do you need to change in order to get rid of the type cast?
		// Why does this work?
		ArrayList arraylist = (ArrayList)new ChildTwo().doStuff();
		assertEquals(arraylist instanceof ArrayList, true);
	}
}

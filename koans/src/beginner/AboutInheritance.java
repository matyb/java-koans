package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.sandwich.koan.Koan;

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
		assertEquals(new Parent().doStuff(), __);
		assertEquals(new Child().doStuff(), __);
		assertEquals(new Child().doStuff("oh no"), __);
	}
	
	abstract class ParentTwo {
		abstract public Collection<?> doStuff();
	}
	
	class ChildTwo extends ParentTwo {
		public Collection<?> doStuff() { return Collections.emptyList(); };
	}
	
	@Koan
	public void overriddenMethodsMayReturnSubtype() {
		// What do you need to change in order to get rid of the type cast?
		// Why does this work?
		List<?> list = (List<?>) new ChildTwo().doStuff();
		assertEquals(list instanceof List, __);
	}
}

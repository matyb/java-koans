package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class AboutCasting {
    @Koan
	public void longPlusInt() {
		int a = 6;
		long b = 10;
		Object c = a + b;
		assertEquals(16, c);
		assertEquals(c instanceof Integer, __);
		assertEquals(c instanceof Long, __);
	}
    
    @Koan
	public void forceIntTypecast() {
		long a = 2147483648L; 
		// What happens if we force a long value into an int?
		int b = (int)a;
		assertEquals(b, __);
	}
   
    @Koan
	public void implicitTypecast() {
    	int a = 1;
    	int b = Integer.MAX_VALUE;
		long c = a + b; 
		assertEquals(c, __);
	}
    
    class Parent { public String doThing() { return "parent style"; } }
    class Child extends Parent { public String doThing() { return "child style"; } }
    
    @Koan
    public void downCastWithInerhitance() {
    	Child child = new Child();
    	Parent parent = child; // Why isn't there an explicit cast?
    	assertEquals(parent instanceof Parent,__);
    	assertEquals(child instanceof Parent,__);
    	assertEquals(child instanceof Child,__);
    }
    
    @Koan
    public void downCastAndPolymophism() {
    	Child child = new Child();
    	Parent parent = child;
    	// Think about the result. Did you expect that? Why?
    	// Think about inheritance, objects, classes and instances.
    	assertEquals(parent.doThing(),__);
    	assertEquals(child.doThing(),__);
    }
    
    @Koan
    public void upCastWithInheritance() {
    	Parent parent = new Child();
    	Child child = (Child)parent; // Why do we need an explicit cast here?
    	assertEquals(parent instanceof Parent,__);
    	assertEquals(child instanceof Parent,__);
    	assertEquals(child instanceof Child,__);
    }
    
    @Koan
    public void upCastAndPolymophism() {
    	Parent parent = new Child();
    	Child child = (Child)parent;
    	// Think about the result. Did you expect that? Why?
    	// How is that different from above?
    	assertEquals(parent.doThing(),__);
    	assertEquals(child.doThing(),__);
    }
    
    class SuperChild extends Child {
    	public String doSuperChildThing() { return "Superchild"; }
    }
    
    @Koan
    public void complicatedCast() {
    	Parent parent = new SuperChild();
    	// What do you need to do in order to call "doSuperChildThing"? 
    	assertEquals("Superchild", __);
    }

    interface Sleepable {
    	String sleep();
    }
    
    class SleepChild extends Child implements Sleepable {
		public String sleep() { return "zzzz"; }
    }
    
    @Koan
    public void complicatedCastWithInterface() {
    	Parent parent = new SleepChild();
    	// What do you need to do in order to call "sleep"? 
    	assertEquals("zzzz", __);
    }

   
}

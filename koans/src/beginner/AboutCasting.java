package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;
import static com.sandwich.util.Assert.fail;

import com.sandwich.koan.Koan;

@SuppressWarnings("unused")
public class AboutCasting {
    
	@Koan
	public void longPlusInt() {
		int a = 6;
		long b = 10;
		Object c = a + b;
		assertEquals(c, __);
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
    
    class GrandParent { public String complain() { return "Get your feet off the davenport!"; } }
    class Parent extends GrandParent { public String complain() { return "TPS reports don't even have a cover letter!"; } }
    
    @Koan
    public void downCastWithInerhitance() {
    	Parent parent = new Parent();
    	GrandParent grandParent = parent; // Why isn't there an explicit cast?
    	assertEquals(grandParent instanceof GrandParent,__);
    	assertEquals(parent instanceof GrandParent,__);
    	assertEquals(parent instanceof Parent,__);
    	// parents know of their parents
    }
    
    @Koan
    public void downCastAndPolymophism() {
    	Parent parent = new Parent();
    	GrandParent grandParent = parent;
    	// Think about the result. Did you expect that? Why?
    	// Think about inheritance, objects, classes and instances.
    	assertEquals(parent.complain(), __);
    	assertEquals(grandParent.complain(), __);
    }
    
    @Koan
    public void upCastWithInheritance() {
    	GrandParent grandParent = new Parent();
    	Parent parent = (Parent)grandParent; // Why do we need an explicit cast here?
    	assertEquals(grandParent instanceof GrandParent,__);
    	assertEquals(parent instanceof GrandParent,__);
    	assertEquals(parent instanceof Parent,__);
    	// a parent does not know of it's children implicitly, it is an open ended contract... 
    	// so YOU need to define that for the compiler with a cast - this can vary at runtime
    }
    
    @Koan
    public void upCastAndPolymophism() {
    	GrandParent grandParent = new GrandParent();
    	Parent parent = (Parent)grandParent;
    	// Think about the result. Did you expect that? Why?
    	// How is that different from above?
    	assertEquals(grandParent.complain(),__);
    	assertEquals(parent.complain(),__);
    }
    
    interface Sleepable {
    	String sleep();
    }
    
    class Child extends Parent implements Sleepable{ 
    	public String praise() { 
    		return "I think you are a great software developer."; 
    	}
    	public String sleep() { 
    		return "zzzz"; 
    	}
    }
    
    @Koan
    public void classCasting(){
    	try{
    		Object o = new Parent(); 	// were downcasting way to far here - would it be possible
    									// to even author this koan had we done what was safe, and 
    									// held the reference as Sleepable?
    		((Sleepable)o).sleep();
    	}catch(ClassCastException x){
    		fail("Parent does not implement Sleepable, maybe one of his kids do?");
    	}
    }
    
    @Koan
    public void complicatedCast() {
    	Parent parent = new Child();
    	// How can we access the stepchild's ability to "praise" - if the reference is held as a superclass? 
    	assertEquals("I think you are a great software developer.", __);
    }
    
    @Koan
    public void complicatedCastWithInterface() {
    	Parent parent = new Child();
    	// What do you need to do in order to call "sleep"? 
    	assertEquals("zzzz", __);
    }

   
}

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
		long c = a + b; // still overflows int... which is the Integer.MIN_VALUE, the operation occurs prior to assignment to long
		assertEquals(c, __);
	}
    
    class Parent extends Child { public String complain() { return "TPS reports don't even have a cover letter!"; } }
    class GrandParent extends Parent { public String complain() { return "Get your feet off the davenport!"; } }
    
    @Koan
    public void downCastWithInerhitance() {
    	GrandParent grandParent = new GrandParent();
    	Parent parentReference = grandParent; // Why isn't there an explicit cast?
    	assertEquals(grandParent instanceof GrandParent, __);
    	assertEquals(parentReference instanceof GrandParent, __);
    	assertEquals(parentReference instanceof Parent, __);
    	assertEquals(parentReference instanceof Child, __);
    }
    
    @Koan
    public void downCastAndPolymophism() {
    	GrandParent grandParent = new GrandParent();
    	Parent parentReference = grandParent;
    	// If the result is unexpected, consider the difference between an instance and its reference
    	assertEquals(parentReference.complain(), __);
    }
    
    @Koan
    public void upCastWithInheritance() {
    	Object grandParent = new GrandParent();
    	Parent parentReference = (Parent)grandParent; // Why do we need an explicit cast here?
    	assertEquals(grandParent instanceof Child,__);
    	assertEquals(parentReference instanceof GrandParent,__);
    	assertEquals(parentReference instanceof Parent,__);
    	assertEquals(parentReference instanceof Child,__);
    }
    
    @Koan
    public void upCastAndPolymophism() {
    	Object grandParent = new GrandParent();
    	Parent parent = (GrandParent)grandParent;
    	// Think about the result. Did you expect that? Why?
    	// How is that different from above?
    	assertEquals(parent.complain(), __);
    }
    
    interface Sleepable {
    	String sleep();
    }
    
    class Child implements Sleepable{
    	public String sleep() { 
    		return "zzzz"; 
    	}
    }
    
    @Koan
    public void classCasting(){
    	try{
    		Object o = new Object(); 	// were downcasting way to far here - would it be possible
    									// to even compile this koan had we done what was safe, and 
    									// held the reference as Sleepable?
    		((Sleepable)o).sleep();
    	}catch(ClassCastException x){
    		fail("Object does not implement Sleepable, maybe one of the people classes do?");
    	}
    }
    
    @Koan
    public void complicatedCast() {
    	Child parent = new Parent();
    	// How can we access the parent's ability to "complain" - if the reference is held as a superclass? 
    	assertEquals(new Parent().complain(), __);
    }
    
    @Koan
    public void complicatedCastWithInterface() {
    	Object parent = new Child();
    	// What do you need to do in order to call "sleep" on the parent? 
    	assertEquals(new Child().sleep(), __);
    }

   
}

package beginner;

import java.io.IOException;

import com.sandwich.koan.Koan;
import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutExceptions {
	
	private void doStuff() throws IOException {
		throw new IOException();
	}
	
	@Koan
	public void catchCheckedExceptions() {
		String s;
		try {
			doStuff();
			s = "code run normally";
		} catch(IOException e) { 
			s = "exception thrown";
		}
		assertEquals(s,__);
	}
	
	@Koan
	public void useFinally() {
		String s = "";
		try {
			doStuff();
			s = "code run normally";
		} catch(IOException e) { 
			s = "exception thrown";
		} finally {
			s += " and finally ran as well";
		}
		assertEquals(s,__);
	}
	
	@Koan
	public void finallyWithoutCatch() {
		String s = "";
		try {
			s = "code run normally";
		} finally {
			s += " and finally ran as well";
		}
		assertEquals(s,__);
	}
	
	private void doUncheckedStuff() {
		throw new RuntimeException();	
	}
	
	@Koan
	public void catchUncheckedExceptions() {
		// What do you need to do to catch the
		// unchecked exception?
		doUncheckedStuff();
	}
	
	@SuppressWarnings("serial")
	static class ParentException extends Exception {}  
	@SuppressWarnings("serial")
	static class ChildException extends ParentException {}
	
	private void throwIt() throws ParentException {
		throw new ChildException();
	}
	
	@Koan
	public void catchOrder() {
		 String s = "";
		try {
			throwIt();
		} catch(ChildException e) {
			s = "ChildException";
		} catch(ParentException e) {
			s = "ParentException";
		}
		assertEquals(s, __);
	}	
}

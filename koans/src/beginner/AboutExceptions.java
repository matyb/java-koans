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
	
	private int k = 1, m = 2;
	
	private void tryCatchFinallyWithVoidReturn() {
		try {
			doStuff();
		} catch(IOException e) { 
			k += 1;
			return;
		} finally {
			// Will this code be executed despite the return statement above?
			k += 1;
		}
	}
	
	@Koan
	public void finallyIsAlwaysRan() {
		tryCatchFinallyWithVoidReturn();
		assertEquals(k,3);
	}
	
	private int tryCatchFinallyReturningInt() {
		try {
			doStuff();
		} catch(IOException e) { 
			k += 1;
			return k + m;
		} finally {
			m = 5;
		}
		return k;
	}
	
	@Koan
	public void orderOfCatchFinallyAndReturn() {
		k = 1;
		assertEquals(tryCatchFinallyReturningInt(),4);
		// What is the order of executing catch, finally
		// and return statements in this case?
		assertEquals(k,2);
		assertEquals(m,5);
	}
	
	private int i = 0, j = 1, l = 2;
	
	private int returnStatementsEverywhere() {
		try {
			doStuff();
			return i++;
		} catch (IOException e) {
			return j++;
		} finally {
			// Watch out! It is an EXTREMELY bad practice to 
			// put return statement in finally block! 
			return l++;
		}
	}
	
	@Koan
	public void returnInFinallyBlock() {
		// Which value will be returned here?
		assertEquals(returnStatementsEverywhere(),2);
		// Is only the returned variable modified?
		assertEquals(i,__);
		assertEquals(j,__);
		assertEquals(l,__);
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

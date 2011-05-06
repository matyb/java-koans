package beginner;

import com.sandwich.koan.Koan;
import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutOperators {

	@Koan
	public void plusPlusVariablePlusPlus(){
		int i = 1;
		assertEquals(++i,__);
		assertEquals(i,__);
		assertEquals(i++,__);
		assertEquals(i,__);
	}
	
	@Koan
	public void shortCircuit() {
		int i = 1;
		int a = 6; // Why did we use a variable here?
		// What happens if you replace 'a' with '6' below?
		// Try this with an IDE like Eclipse...
		if ( (a < 9 ) || (++i < 8)  )  i = i + 1;
		assertEquals(i,__);
	}
	
	@Koan
	public void fullAnd(){
		int i = 1;
		if ( true & (++i < 8)  )  i = i + 1;
		assertEquals(i,__);
	}
	
	@Koan
	public void shortCircuitAnd(){
		int i = 1;
		if ( true && (i < -28)  )  i = i + 1;
		assertEquals(i,__);
	}
	
	@Koan
	public void aboutXOR() {
		int i = 1;
		int a = 6;
		if ( (a < 9 ) ^ false)  i = i + 1;
		assertEquals(i,__);
	}
	
	@Koan
	public void dontMistakeEqualsForEqualsEquals() {
		int i = 1;
		boolean a = false;
		if (a = true)  i++;
		assertEquals(a, __);
		assertEquals(i,__);
		// How could you write the condition 'with a twist' to avoid this trap?
	}
	
}

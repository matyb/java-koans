package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class AboutPrimitives {
	@Koan()
	public void byteSize() {
		assertEquals(Byte.SIZE, __);
	}
	
	@Koan()
	public void shortSize() {
		assertEquals(Short.SIZE, __);
	}
	
	@Koan
	public void integerSize() {
		assertEquals(Integer.SIZE, __);
	}
	
	@Koan
	public void longSize() {
		assertEquals(Long.SIZE, __);
	}
	
	@Koan
	public void charSizeAndValue() {
		// a char basically is an unsigned int
		assertEquals(Character.SIZE,__);
		assertEquals(Character.MIN_VALUE,__);
		assertEquals(Character.MAX_VALUE,__);
	}
	
	// Floating Points
	@Koan
	public void floatSize() {
		assertEquals(Float.SIZE,__);
	}
	
	@Koan
	public void doubleSize() {
		assertEquals(Double.SIZE, __);
	}
}

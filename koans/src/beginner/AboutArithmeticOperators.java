package beginner;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutArithmeticOperators {

    @Koan
    public void simpleOperations() {
        assertEquals(1, __);
        assertEquals(1 + 1, __);
        assertEquals(2 + 3 * 4, __);
        assertEquals((2 + 3) * 4, __);
        assertEquals(2 * 3 + 4, __);
        assertEquals(2 - 3 + 4, __);
        assertEquals(2 + 4 / 2, __);
        assertEquals((2 + 4) / 2, __);
    }

    @Koan
    public void notSoSimpleOperations() {
        assertEquals(1 / 2, __);
        assertEquals(3 / 2, __);
        assertEquals(1 % 2, __);
        assertEquals(3 % 2, __);
    }

    @Koan
    public void minusMinusVariableMinusMinus() {
        int i = 1;
        assertEquals(--i, __);
        assertEquals(i, __);
        assertEquals(i--, __);
        assertEquals(i, __);
    }

    @Koan
    public void plusPlusVariablePlusPlus() {
        int i = 1;
        assertEquals(++i, __);
        assertEquals(i, __);
        assertEquals(i++, __);
        assertEquals(i, __);
    }

    @Koan
    public void timesAndDivInPlace() {
        int i = 1;
        i *= 2;
        assertEquals(i, __);
        i /= 2;
        assertEquals(i, __);
    }

}

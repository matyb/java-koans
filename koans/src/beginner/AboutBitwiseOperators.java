package beginner;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutBitwiseOperators {

    @Koan
    public void fullAnd() {
        int i = 1;
        if (true & (++i < 8)) i = i + 1;
        assertEquals(i, 3);
    }

    @Koan
    public void shortCircuitAnd() {
        int i = 1;
        if (true && (i < -28)) i = i + 1;
        assertEquals(i, 1);
    }

    @Koan
    public void aboutXOR() {
        int i = 1;
        int a = 6;
        if ((a < 9) ^ false) i = i + 1;
        assertEquals(i, 2);
    }

    @Koan
    public void dontMistakeEqualsForEqualsEquals() {
        int i = 1;
        boolean a = false;
        if (a = true) i++;
        assertEquals(a, true);
        assertEquals(i, 2);
        // How could you write the condition 'with a twist' to avoid this trap?
        // ANSWER: invert the condition and do true = a. It will not compile.
    }

    @Koan
    public void aboutBitShiftingRightShift() {
        int rightShift = 8;
        rightShift = rightShift >> 1;
        assertEquals(rightShift, 4);
    }

    @Koan
    public void aboutBitShiftingLeftShift() {
        int leftShift = 0x80000000; // Is this number positive or negative?
        leftShift = leftShift << 1;
        assertEquals(leftShift, 0);
    }

    @Koan
    public void aboutBitShiftingRightUnsigned() {
        int rightShiftNegativeStaysNegative = 0x80000000;
        rightShiftNegativeStaysNegative = rightShiftNegativeStaysNegative >> 4;
        assertEquals(rightShiftNegativeStaysNegative, -134217728);
        int unsignedRightShift = 0x80000000; // always fills with 0
        unsignedRightShift >>>= 4; // Just like +=
        assertEquals(unsignedRightShift, 134217728);
    }
}

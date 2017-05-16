package java7;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutJava7LiteralsEnhancements {

    @Koan
    public void binaryLiterals() {
        //binary literals are marked with 0b prefix
        short binaryLiteral = 0b1111;
        assertEquals(binaryLiteral, (short)15);
    }

    @Koan
    public void binaryLiteralsWithUnderscores() {
        //literals can use underscores for improved readability
        short binaryLiteral = 0b1111_1111;
        assertEquals(binaryLiteral, (short)255);
    }

    @Koan
    public void numericLiteralsWithUnderscores() {
        long literal = 111_111_111L;
        //notice capital "B" - a valid binary literal prefix
        short multiplier = 0B1_000;
        assertEquals(literal * multiplier, (long)888888888);
    }

    @Koan
    public void negativeBinaryLiteral() {
        int negativeBinaryLiteral = 0b1111_1111_1111_1111_1111_1111_1111_1100 / 4;
        assertEquals(negativeBinaryLiteral, -1);
    }

    @Koan
    public void binaryLiteralsWithBitwiseOperator() {
        int binaryLiteral = ~0b1111_1111;
        assertEquals(binaryLiteral, -256);
    }

}

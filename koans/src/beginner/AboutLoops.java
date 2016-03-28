package beginner;


import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;


public class AboutLoops {

    @Koan
    public void basicForLoop1() {
        String s = "";
        for (int i = 0; i < 5; i++) {
            s += i + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void basicForLoop2() {
        String s = "";
        for (int i = -5; i < 1; i++) {
            s += i + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void basicForLoop3() {
        String s = "";
        for (int i = 5; i > 0; i--) {
            s += i + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void basicForLoop4() {
        String s = "";
        for (int i = 0; i < 11; i += 2) {
            s += i + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void basicForLoop5() {
        String s = "";
        for (int i = 1; i <= 16; i *= 2) {
            s += i + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void basicForLoopWithTwoVariables1() {
        String s = "";
        for (int i = 0, j = 10; i < 5 && j > 5; i++, j--) {
            s += i + " " + j + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void nestedLoops() {
        String s = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                s += "(" + i + ", " + j + ") ";
            }
            s += " - ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void extendedForLoop() {
        int[] is = {1, 2, 3, 4};
        String s = "";
        for (int j : is) {
            s += j + " ";
        }
        assertEquals(s, __);
    }

    @Koan
    public void whileLoop() {
        int result = 0;
        while (result < 3) {
            result++;
        }
        assertEquals(result, __);
    }

    @Koan
    public void doLoop() {
        int result = 0;
        do {
            result++;
        } while (false);
        assertEquals(result, __);
    }

    @Koan
    public void extendedForLoopBreak() {
        String[] sa = {"Dog", "Cat", "Tiger"};
        int count = 0;
        for (String current : sa) {
            if ("Cat".equals(current)) {
                break;
            }
            count++;
        }
        assertEquals(count, __);
    }

    @Koan
    public void extendedForLoopContinue() {
        String[] sa = {"Dog", "Cat", "Tiger"};
        int count = 0;
        for (String current : sa) {
            if ("Dog".equals(current)) {
                continue;
            } else {
                count++;
            }
        }
        assertEquals(count, __);
    }

    @Koan
    public void forLoopContinueLabel() {
        int count = 0;
        outerLabel:
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                count++;
                if (count > 2) {
                    continue outerLabel;
                }
            }
            count += 10;
        }
        // What does continue with a label mean?
        // What gets executed? Where does the program flow continue?
        assertEquals(count, __);
    }

    @Koan
    public void forLoopBreakLabel() {
        int count = 0;
        outerLabel:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                count++;
                if (count > 2) {
                    break outerLabel;
                }
            }
            count += 10;
        }
        // What does break with a label mean?
        // What gets executed? Where does the program flow continue?
        assertEquals(count, __);
    }
}

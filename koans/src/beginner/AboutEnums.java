package beginner;

import com.sandwich.koan.Koan;

import static beginner.AboutEnums.Color.Red;
import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutEnums {

    enum Color {
        Red, Blue, Green, Yellow // what happens if you add a ; here?
        // What happens if you type Red() instead?
    }

    @Koan
    public void basicEnums() {
        Color blue = Color.Blue;
        assertEquals(blue == Color.Blue, __);
        assertEquals(blue == Red, __);
        assertEquals(blue instanceof Color, __);
    }

    @Koan
    public void basicEnumsAccess() {
        Color[] colorArray = Color.values();
        assertEquals(colorArray[0], __);
        assertEquals(colorArray[2], __);
    }

    private String colorsIKnow(Color color) {
        switch (color) {
            case Red: return "red";
            case Green: return "green";
            default : return "unknown";
        }
    }

    @Koan
    public void switchWithEnums() {
        assertEquals(colorsIKnow(Color.Red), __);
        assertEquals(colorsIKnow(Color.Blue), __);
    }

    @Koan
    public void iterationWithEnums() {
        String result = "";
        for (Color color : Color.values()) {
            result += color.name();
        }

        assertEquals(result, __);
    }

    enum SkatSuit {
        Clubs(12), Spades(11), Hearts(10), Diamonds(9);

        SkatSuit(int v) {
            value = v;
        }

        private int value;
    }

    @Koan
    public void enumsWithAttributes() {
        // value is private but we still can access it. Why?
        // Try moving the enum outside the AboutEnum class... What do you expect?
        // What happens?
        assertEquals(SkatSuit.Clubs.value > SkatSuit.Spades.value, __);
    }

    enum OpticalMedia {
        CD(650), DVD(4300), BluRay(50000);

        OpticalMedia(int c) {
            capacityInMegaBytes = c;
        }

        int capacityInMegaBytes;

        int getCoolnessFactor() {
            return (capacityInMegaBytes - 1000) * 10;
        }
    }

    @Koan
    public void enumsWithMethods() {
        assertEquals(OpticalMedia.CD.getCoolnessFactor(), __);
        assertEquals(OpticalMedia.BluRay.getCoolnessFactor(), __);
    }
}

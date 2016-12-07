package java7;

import com.sandwich.koan.Koan;

import java.util.Objects;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutRequireNotNull {

    @Koan
    public void failArgumentValidationWithRequireNotNull() {
        // This koan demonstrates the use of Objects.requireNotNull
        // in place of traditional argument validation using exceptions
        String s = "";
        try {
            s += validateUsingRequireNotNull(null);
        } catch (NullPointerException ex) {
            s = "caught a NullPointerException";
        }
        assertEquals(s, __);
    }

    @Koan
    public void passArgumentValidationWithRequireNotNull() {
        // This koan demonstrates the use of Objects.requireNotNull
        // in place of traditional argument validation using exceptions
        String s = "";
        try {
            s += validateUsingRequireNotNull("valid");
        } catch (NullPointerException ex) {
            s = "caught a NullPointerException";
        }
        assertEquals(s, __);
    }

    private int validateUsingRequireNotNull(String str) {
        // If you're only concerned with null values requireNotNull
        // is concise and the point of the NullPointerException it
        // throws is clear, though you can optionally provide a
        // description as well
        return Objects.requireNonNull(str).length();
    }

}

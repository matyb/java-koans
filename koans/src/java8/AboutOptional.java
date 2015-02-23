package java8;

import com.sandwich.koan.Koan;

import java.util.Optional;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutOptional {

    boolean optionalIsPresentField = false;
    @Koan
    public void isPresent() {
        boolean optionalIsPresent = false;
        Optional<String> value = notPresent();
        if (value.isPresent()) {
            optionalIsPresent = true;
        }
        assertEquals(optionalIsPresent, __);
    }

    @Koan
    public void ifPresentLambda() {
        Optional<String> value = notPresent();
        value.ifPresent( x -> optionalIsPresentField = true);
        assertEquals(optionalIsPresentField, __);
    }

    //use optional on api to signal that value is optional
    public Optional<String> notPresent() {
        return Optional.empty();
    }

    private Optional<String> present() {
        return Optional.of("is present");
    }
}

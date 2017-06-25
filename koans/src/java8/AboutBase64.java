package java8;

import com.sandwich.koan.Koan;

import java.util.Base64;
import java.io.UnsupportedEncodingException;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutBase64 {

    private final String plainText = "lorem ipsum";
    private final String encodedText = "bG9yZW0gaXBzdW0=";

    @Koan
    public void base64Encoding() {
        try {
            // Encode the plainText
            // This uses the basic Base64 encoding scheme but there are corresponding
            // getMimeEncoder and getUrlEncoder methods available if you require a
            // different format/Base64 Alphabet 
            assertEquals(encodedText, Base64.getEncoder().encodeToString(__.getBytes("utf-8")));
        } catch (UnsupportedEncodingException ex) {}
    }

    @Koan
    public void base64Decoding() {
        // Decode the Base64 encodedText
        // This uses the basic Base64 decoding scheme but there are corresponding
        // getMimeDecoder and getUrlDecoder methods available if you require a
        // different format/Base64 Alphabet
        byte[] decodedBytes = Base64.getDecoder().decode(__);
        try {
            String decodedText = new String(decodedBytes, "utf-8");
            assertEquals(plainText, decodedText);
        } catch (UnsupportedEncodingException ex) {}
    }

}

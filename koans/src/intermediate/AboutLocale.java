package intermediate;

import com.sandwich.koan.Koan;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutLocale {

    @Koan
    public void localizedOutputOfDates() {
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 3, 3);
        Date date = cal.getTime();
        Locale localeBR = new Locale("pt", "BR"); // portuguese, Brazil
        DateFormat dateformatBR = DateFormat.getDateInstance(DateFormat.FULL, localeBR);
        assertEquals(dateformatBR.format(date), "Domingo, 3 de Abril de 2011");

        Locale localeJA = new Locale("ja"); // Japan
        DateFormat dateformatJA = DateFormat.getDateInstance(DateFormat.FULL, localeJA);
        // Well if you don't know how to type these characters, try "de", "it" or "us" ;-)
        assertEquals(dateformatJA.format(date), "2011年4月3日");
    }

    @Koan
    public void getCountryInformation() {
        Locale locBR = new Locale("pt", "BR");
        assertEquals(locBR.getDisplayCountry(), "Brazil");
        assertEquals(locBR.getDisplayCountry(locBR), "Brasil");

        Locale locCH = new Locale("it", "CH");
        assertEquals(locCH.getDisplayCountry(), "Switzerland");
        assertEquals(locCH.getDisplayCountry(locCH), "Svizzera");
        assertEquals(locCH.getDisplayCountry(new Locale("de", "CH")), "Schweiz");
    }

    @Koan
    public void formatCurrency() {
        float someAmount = 442.23f; // Don't use floats for money in real life. Really. It's a bad idea.
        Locale locBR = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locBR);
        assertEquals(nf.format(someAmount), "R$ 442,23");
    }
}

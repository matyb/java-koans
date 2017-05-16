package java8;

import com.sandwich.koan.Koan;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutLocalTime {

    @Koan
    public void localTime() {
        LocalTime t1 = LocalTime.of(7, 30);
        assertEquals(t1, LocalTime.parse("07:30"));
    }

    @Koan
    public void localTimeMinus() {
        LocalTime t1 = LocalTime.parse("10:30");
        LocalTime t2 = t1.minus(2, ChronoUnit.HOURS);
        assertEquals(t2, LocalTime.parse("08:30"));
    }

}

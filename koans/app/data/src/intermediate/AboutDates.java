package intermediate;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sandwich.koan.Koan;


public class AboutDates {
	
	private Date date = new Date(100010001000L);
	
	@Koan
	public void dateToString() {
		assertEquals(date.toString(), __);
	}
	
	@Koan
	public void changingDateValue() {
		int oneHourInMiliseconds = 3600000;
		date.setTime(date.getTime() + oneHourInMiliseconds);
		assertEquals(date.toString(), __);
	}
	
	@Koan
	public void usingCalendarToChangeDates() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		assertEquals(cal.getTime().toString(), __);
	}

	@Koan
	public void usingRollToChangeDatesDoesntWrapOtherFields() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.roll(Calendar.MONTH, 12);
		assertEquals(cal.getTime().toString(), __);
	}
	
	@Koan
	public void usingDateFormatToFormatDate() {
		String formattedDate = DateFormat.getDateInstance().format(date);
		assertEquals(formattedDate, __);
	}
	
	@Koan
	public void usingDateFormatToFormatDateShort() {
		String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
		assertEquals(formattedDate, __);
	}
	
	@Koan
	public void usingDateFormatToFormatDateFull() {
		String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
		// There is also DateFormat.MEDIUM and DateFormat.LONG... you get the idea ;-)
		assertEquals(formattedDate, __);
	}
	
	@Koan
	public void usingDateFormatToParseDates() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date2 = dateFormat.parse("01-01-2000");
		assertEquals(date2.toString(), __);
		// What happened to the time? What do you need to change to keep the time as well?
	}
}

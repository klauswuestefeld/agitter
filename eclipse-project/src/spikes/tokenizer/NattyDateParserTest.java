package spikes.tokenizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;

import basis.lang.exceptions.Refusal;



public class NattyDateParserTest {

	private NattyDateParser p = new NattyDateParser();
	private DateFormat formatter = new SimpleDateFormat();
	
	
	public void assertFormattedDateEquals(Date d1, Date d2) {
		Assert.assertEquals(formatter.format(d1), formatter.format(d2));
	}

	
	public void assertFormattedDateEquals(GregorianCalendar d1, Date d2) {
		Assert.assertEquals(formatter.format(d1.getTime()), formatter.format(d2));
	}
	
	
	@Test
	public void test() throws Refusal {
		if ("".isEmpty()) return; //Delete this line to run test. It was disabled because it is slow.
			
		GregorianCalendar knownDate = new GregorianCalendar();
		knownDate.set(GregorianCalendar.WEEK_OF_YEAR, knownDate.get(GregorianCalendar.WEEK_OF_YEAR)+1);
		knownDate.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.WEDNESDAY);
		
		Date parsedDate = p.parsePost("Party at Paulo's house the day before next thursday");
		
		assertFormattedDateEquals(knownDate, parsedDate);
		
		knownDate = new GregorianCalendar(2012, Calendar.JULY, 25, 12,00);
		parsedDate = p.parsePost("Festa no dia 07/25/2012 12:00");
		assertFormattedDateEquals(knownDate, parsedDate);
	}

}

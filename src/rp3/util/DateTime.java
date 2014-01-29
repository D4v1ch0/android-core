package rp3.util;

import java.util.Calendar;
import java.util.Date;

public class DateTime {

	public static Date getCurrentDateTime(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
}

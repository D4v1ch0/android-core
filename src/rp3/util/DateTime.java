package rp3.util;

import java.util.Calendar;
import java.util.Date;

public class DateTime {

	public static final long TICKS_AT_EPOCH = 621355968000000000L;
    public static final long TICKS_PER_MILLISECOND = 10000;
	
	public static final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
	
	public static long getTicksFromDotNet(long ticks){
		Calendar c = Calendar.getInstance();
		long jTicks = (ticks - DateTime.TICKS_AT_EPOCH) / DateTime.TICKS_PER_MILLISECOND - c.get(Calendar.ZONE_OFFSET);		
		return jTicks;
	}
	
	public static long getDotNetTicks(long ticks){				
		//TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar c = Calendar.getInstance();
        		
		return DateTime.TICKS_AT_EPOCH + ((ticks + c.get(Calendar.ZONE_OFFSET) ) * DateTime.TICKS_PER_MILLISECOND);
	}
	
	public static Date getCurrentDateTime(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public static Calendar getCurrentCalendar(){
		Calendar cal = Calendar.getInstance();
		return cal;
	}
	
	public static long getDaysDiff(Date dateStart, Date dateEnd){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dateStart);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(dateEnd);
		
		// Optional: avoid cloning objects if it is the same day
	    if(c1.get(Calendar.ERA) == c2.get(Calendar.ERA) 
	            && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
	            && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
	        return 0;
	    }
	    else{
	    	c1.set(Calendar.MINUTE, 0);
	    	c1.set(Calendar.SECOND, 0);
	    	c1.set(Calendar.HOUR, 0);
	    	c1.set(Calendar.MILLISECOND, 0);
	    	
	    	c2.set(Calendar.MINUTE, 0);
	    	c2.set(Calendar.SECOND, 0);
	    	c2.set(Calendar.HOUR, 0);
	    	c2.set(Calendar.MILLISECOND, 0);
	    	
	    	long diff = c2.getTimeInMillis() - c1.getTimeInMillis();
		    double days = Double.parseDouble(String.valueOf(diff)) / Double.parseDouble(String.valueOf(MILLISECS_PER_DAY));
		    return (long)Math.ceil(days);
	    }		
	}
}

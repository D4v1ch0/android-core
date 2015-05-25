package rp3.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {
	
	public final static String DEFAULT_FORMAT_DATETIME = "yyyyMMdd HH:mm:ss";
	
	public final static String[] getStringArrayFromScalar(String value){
		String[] values = null;
		if(value!=null) values = new String[] { value };
		return values;
	}
	
	public final static String[] getStringArrayFromScalar(double value){
		return getStringArrayFromScalar(String.valueOf(value));
	}
	
	public final static String[] getStringArrayFromScalar(int value){
		return getStringArrayFromScalar(String.valueOf(value));
	}
	
	public final static String[] getStringArrayFromScalar(float value){
		return getStringArrayFromScalar(String.valueOf(value));
	}
	
	public final static String[] getStringArrayFromScalar(long value){
		return getStringArrayFromScalar(String.valueOf(value));
	}
	
	public final static String[] getStringArrayFromScalar(boolean value){
		return getStringArrayFromScalar(Format.getDataBaseBoolean(value));
	}
	
	public final static Date getDateFromDotNetTicks(long ticks){
		return new Date(DateTime.getTicksFromDotNet(ticks));
	}
	
	public final static Date getDateFromTicks(long ticks){
		return new Date(ticks);
	}
	
	public final static Date getDateFromString(String dateStr)
	{
		try {
			return new SimpleDateFormat(DEFAULT_FORMAT_DATETIME).parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
	
	public final static Long getTicksFromDate(Date date){
		if(date!=null)
			return date.getTime();
		return null;
	}
	
	public final static Long getDotNetTicksFromDate(Date date){
		if(date!=null){
	        long ticks = date.getTime();
			ticks = DateTime.getDotNetTicks(ticks);
			return ticks;
		}
			
		return null;
	}
	
	public final static double getDouble(String value){
		try{
			return Double.parseDouble(value);
		}catch(NumberFormatException ex){
			return 0;
		}
	}
	
	public final static int getInt(String value){
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException ex){
			return 0;
		}
	}
	
	public final static long getLong(String value){
		try{
			return Long.parseLong(value);
		}catch(NumberFormatException ex){
			return 0;
		}
	}
	
	public final static boolean getBoolean(String value){
		if( value.toLowerCase().equals("true") || value.toLowerCase().equals("1"))
			return true;
		return false;
	}
	
	public final static double getDouble(Object value){
		return getDouble(String.valueOf(value));
	}
	
	public final static int getInt(Object value){
		return getInt(String.valueOf(value));
	}
	
	public final static long getLong(Object value){
		return getLong(String.valueOf(value));
	}
	
	public final static String getString(long value){
		return String.valueOf(value);
	}
	
	public final static String getString(int value){
		return String.valueOf(value);
	}
	
	public final static String getString(double value){
		return String.valueOf(value);
	}
	
	public final static String getString(Object value){
		if(value!=null)
			return value.toString();
		else 
			return null;		
	}

    public final static Integer versionCompare(String str1, String str2)
    {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]))
        {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length)
        {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else
        {
            return Integer.signum(vals1.length - vals2.length);
        }
    }
}

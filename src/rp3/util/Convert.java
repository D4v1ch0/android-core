package rp3.util;

import java.util.Date;

public class Convert {
	
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
	
	public final static Date getDateFromTicks(long ticks){
		return new Date(ticks);
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
}

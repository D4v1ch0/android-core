package rp3.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public final class Format {

	public final static String FORMAT_DATABASE_DATETIME = "yyyyMMdd HH:mm:ss";
	
	public final static String getDefaultCurrencyFormat(String value)
	{
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(value);
	}
	
	public final static String getDefaultCurrencyFormat(float value)
	{
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(value);
	}
	
	public final static String getDefaultCurrencyFormat(double value)
	{				
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(value);
	}
	
	public final static String getDefaultNumberFormat(float value)
	{
		NumberFormat format = NumberFormat.getNumberInstance();
		return format.format(value);
	}
	
	public final static String getDefaultNumberFormat(int value)
	{
		NumberFormat format = NumberFormat.getNumberInstance();
		return format.format(value);
	}
	
	public final static String getDefaultNumberFormat(double value)
	{
		NumberFormat format = NumberFormat.getNumberInstance();
		return format.format(value);
	}
	
	public final static String getMediumDateFormat(Context context, String dateStr)
	{
		return DateFormat.getMediumDateFormat(context).
				format(getDateFromDataBaseString(dateStr));
	}
	
	public final static String getDefaultDateFormat(Date date)
	{
		java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
		if(date !=null)
			return dateFormat.format(date);
		return "";
	}
	
	
	public final static String getDataBaseString(Date dateToString)
	{
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATABASE_DATETIME);				
		return df.format(dateToString);
	}
	
	public final static Date getDateFromDataBaseString(String dateStr)
	{
		try {
			return new SimpleDateFormat(FORMAT_DATABASE_DATETIME).parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public final static boolean getBooleanFromDataBaseInteger(int boolInt){
		return boolInt == 1? true: false;
	}
	
	public final static int getDataBaseBoolean(boolean value){
		return value ? 1: 0;  
	}
			
}

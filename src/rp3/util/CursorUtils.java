package rp3.util;

import java.util.Date;

import android.database.Cursor;

public class CursorUtils {

	public static int getInt(Cursor c){
		int value = 0;
		if(c.moveToFirst()){
			value = c.getInt(0);
		}
		return value;
	}
	
	public static long getLong(Cursor c){
		long value = 0;
		if(c.moveToFirst()){
			value = c.getLong(0);
		}
		return value;
	}
	
	public static double getDouble(Cursor c){
		double value = 0;
		if(c.moveToFirst()){
			value = c.getDouble(0);
		}
		return value;
	}
	
	public static String getString(Cursor c){
		String value = null;
		if(c.moveToFirst()){
			value = c.getString(0);
		}
		return value;
	}
	
	public static String getString(Cursor c, String columnName){
		return c.getString( c.getColumnIndex(columnName) );
	}
	
	public static boolean getBoolean(Cursor c, String columnName){
		return c.getShort( c.getColumnIndex(columnName) ) == 1;
	}
	
	public static int getInt(Cursor c, String columnName){
		return c.getInt( c.getColumnIndex(columnName) );
	}
	
	public static long getLong(Cursor c, String columnName){
		return c.getLong( c.getColumnIndex(columnName) );
	}
	
	public static double getDouble(Cursor c, String columnName){
		return c.getDouble( c.getColumnIndex(columnName) );
	}
	
	public static short getShort(Cursor c, String columnName){
		return c.getShort( c.getColumnIndex(columnName) );
	}
	
	public static float getFloat(Cursor c, String columnName){
		return c.getFloat( c.getColumnIndex(columnName) );
	}
	
	public static Date getDate(Cursor c, String columnName){
		long time = c.getLong( c.getColumnIndex(columnName) );
		return Convert.getDateFromTicks(time);
	}
	
}

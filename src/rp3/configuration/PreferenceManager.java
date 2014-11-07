package rp3.configuration;

import java.util.Date;

import rp3.runtime.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public abstract class PreferenceManager {

	private static SharedPreferences preferences;	
		
	PreferenceManager(){
	}
	
	public static String getResultValue(String value){		
		
		while(value.contains("[@"))
		{
			int initial = value.indexOf("[@");			
			int end = value.indexOf("]");
			String parameter = value.substring(initial + 2, end);
			String key = parameter;
			String defaultValue = ""; 
			if(parameter.contains(":")){
				String[] parts = parameter.split(":");
				key = parts[0];
				defaultValue = parameter.substring(parameter.indexOf(key) + key.length()+1);
			}
			String keyValue = PreferenceManager.getString(key);
			if(TextUtils.isEmpty(keyValue)){
				setValue(key, defaultValue);
				keyValue = defaultValue;
			}
			
			value = value.replace("[@" + parameter + "]", keyValue);			
		}
		return value;
	}
	
	public static void close(){
		preferences = null;
	}
	
	
	public static SharedPreferences getPreferences(){
		if(preferences == null)
			preferences = Session.getContext().getSharedPreferences(Session.getContext().getApplicationContext().getPackageName(), 
					Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
			//preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(Session.getContext());
		
		return preferences;
	}
	
	static Context getContext(){
		return Session.getContext();
	}
	
	public static void setValue(String key, String value){		
		getPreferences().edit().putString(key, value).commit();
	}
	
	public static void setValue(String key, Date value){		
		getPreferences().edit().putLong(key, value.getTime()).commit();
	}
	
	public static void setValue(String key, boolean value){		
		getPreferences().edit().putBoolean(key, value).commit();
	}
	
	public static void setValue(String key, int value){
		getPreferences().edit().putInt(key, value).commit();		
	}
	
	public static void setValue(String key, float value){
		getPreferences().edit().putFloat(key, value).commit();
	}
	
	public static long getLong(String key){
		return getPreferences().getLong(key, 0);
	}
	
	public static int getInt(String key){
		return getPreferences().getInt(key, 0);
	}
	
	public static float getFloat(String key){
		return getPreferences().getFloat(key, 0);		
	}
	
	public static boolean getBoolean(String key){
		return getPreferences().getBoolean(key, false);
	}
	
	public static String getString(String key){
		return getPreferences().getString(key, null);
	}
	
	public static long getLong(String key, long defaultValue){
		return getPreferences().getLong(key, defaultValue);
	}
	
	public static int getInt(String key, int defaultValue){
		return getPreferences().getInt(key, defaultValue);
	}
	
	public static float getFloat(String key, float defaultValue){
		return getPreferences().getFloat(key, defaultValue);
	}
	
	public static boolean getBoolean(String key, boolean defaultValue){
		return getPreferences().getBoolean(key, defaultValue);
	}
	
	public static String getString(String key, String defaultValue){
		return getPreferences().getString(key, defaultValue);
	}
}

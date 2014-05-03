package rp3.configuration;

import java.util.Date;

import rp3.runtime.Session;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class PreferenceManager {

	private static SharedPreferences preferences;	
		
	PreferenceManager(){			
	}
	
	static SharedPreferences getPreferences(){
		if(preferences == null)
			preferences = getContext().getSharedPreferences(
					getContext().getPackageName(), Context.MODE_PRIVATE);
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
}

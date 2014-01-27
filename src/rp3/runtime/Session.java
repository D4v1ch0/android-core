package rp3.runtime;

import rp3.security.activity.LoginActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class Session {

	final static String USER_PREFERENCE = "USER";
	final static String PASSWORD_PREFERENCE = "PASSWORD";
	final static String PREFERENCE_NAME = "rp3.security";
	
	public static void StartLoginView(Context context)
    {
		Intent intent = new Intent(context, LoginActivity.class);
		((Activity) context).startActivityForResult(intent, rp3.data.Constants.LOGIN_REQUEST_CODE);
    }
	
	public static boolean Start(Context context)
	{		
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		String user = preferences.getString(USER_PREFERENCE, "");
		String password = preferences.getString(PASSWORD_PREFERENCE, "");
		
		return Start(context,user,password);
	}
	
	public static boolean Start(Context context, String user, String password)
	{		
		if(!user.equals(""))		
			return User.Login(user, password, context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE));
		
		return false;
	}
	
	public static class User {
		
		private static rp3.security.User instance = null;		
		
		private static rp3.security.User getInstance()
		{
			if(instance == null)
			{
				instance = new rp3.security.User();
			}
			return instance;
		}	
		
		public static boolean Login(String user,String password, SharedPreferences preferences)
		{			
			getInstance().setUser(user);
			getInstance().setPassword(password);
			
			if(preferences !=null)
			{
				Editor editPreferences = preferences.edit();
				editPreferences.putString(Session.USER_PREFERENCE, user);
				editPreferences.putString(Session.PASSWORD_PREFERENCE, password);
				editPreferences.commit();
			} 
			
			return true;
		}
		
		public static String getUser()
		{
			return getInstance().getUser();
		}	
			
	}
}

package rp3.runtime;

import rp3.accounts.User;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public final class Session {
	private static final String TAG = Session.class.getSimpleName();
	final static String USER_PREFERENCE = "USER";
	final static String PASSWORD_PREFERENCE = "PASSWORD";
	final static String PREFERENCE_NAME = "rp3.security";	
	
	private static User user;	
	private static Context context;
	
	public static String getDeviceId(){
		TelephonyManager mngr = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE); 
		return mngr.getDeviceId();
	}
	
	public static User getUser(){
		if(user == null) user = User.getCurrentUser(getContext());
		Log.d(TAG,"getUser:"+user.toString());
		return user;
	}
	
	public static void logOut(){							
		user = User.getCurrentUser(getContext());
		Log.d(TAG,"logGout:"+user.toString());
		if(user!=null){
			user.isLogged(false);
			//user.invalidateAuthToken();
		}

	}
	
	public static Context getContext(){
		return context;
	}
	
	public static boolean IsLogged(){				
		return Session.getUser().isLogged();
	}
	
	public static boolean Start(Context c){		
		context = c;
		return true;
	}	
		
	
}

package rp3.accounts;

import org.json.JSONObject;

import rp3.connection.WebService;
import rp3.runtime.Session;
import rp3.sync.TestConnection;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;

public class DefaultServerAuthenticate implements ServerAuthenticate {
    private static final String TAG = DefaultServerAuthenticate.class.getSimpleName();
	public static String KEY_FULL_NAME = "Name";

	public Bundle signUp(final String name, final String email, final String pass, String authType){
		return null;
	}
	
    public Bundle signIn(final String user, final String pass, String authType) {
        Log.d(TAG,"signIn:logonName:"+user+" Pass:"+pass+" AauthType:"+authType);
        WebService method = new WebService();
        Bundle bundle = new Bundle();
        if(TestConnection.executeSync()) {
            Log.d(TAG,"TestConnection:True...");
            method.setConfigurationName("Core", "SignIn");
            method.setAuthTokenType(authType);

            method.addParameter("LogonName", user);
            method.addParameter("Password", pass);

            bundle = new Bundle();


            try {
                method.invokeWebService();

                JSONObject response = method.getJSONObjectResponse();
                String authToken = null;
                String fullName = null;

                if (!response.getJSONObject("Data").isNull("AuthToken")) {
                    Log.d(TAG,"Autenicacion server:"+response.getJSONObject("Data").toString());
                    Log.d(TAG,"!response.getJSONObject(\"Data\").NoisNull...");
                    authToken = response.getJSONObject("Data").getString("AuthToken");
                    fullName = response.getJSONObject("Data").getString("Name");
                    Session.getUser().setFullName(fullName);
                    //Session.getUser().setAuthToken(AccountManager.KEY_AUTHTOKENauthToken);
                }else{
                    Log.d(TAG,"response.getJSONObject(\"Data\").isNull....");
                }

                if (!response.isNull("Message"))
                    bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, response.getJSONObject("Message").getString("Text"));

                bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, authType);
                bundle.putString(KEY_FULL_NAME, fullName);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, response.getJSONObject("Data").getBoolean("IsValid"));
            } catch (Exception e) {
                Log.d(TAG,"Exception...");
                System.out.print(e);
                bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, e.getMessage());
                bundle.putString(AccountManager.KEY_AUTHTOKEN, null);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, false);
            }
        }
        else
        {
            Log.d(TAG,"TestConnection:False...");
            if(Session.IsLogged())
            {
                Log.d(TAG,"Session.IsLogged()...");
                //bundle.putString(AccountManager.KEY_AUTHTOKEN, Session.getUser().getAuthToken(authType));
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, authType);
                bundle.putString(KEY_FULL_NAME, Session.getUser().getFullName());
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, true);
            }
            else {
                Log.d(TAG,"Session.NotIsLogged()...");
                bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, "No hay conexión al servidor");
                bundle.putString(AccountManager.KEY_AUTHTOKEN, null);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, false);
            }
        }
		
    	return bundle;	       	    	
    }

	@Override
	public boolean requestSignIn() {		
		Bundle data = signIn(Session.getUser().getLogonName(), Session.getUser().getPassword(), User.getAccountType());
		if(data.getBoolean(ServerAuthenticate.KEY_SUCCESS))
		{
		    Log.d(TAG,"data.getBoolean(ServerAuthenticate.KEY_SUCCESS==True...");
            String token = "temp";
            if(data.containsKey(AccountManager.KEY_AUTHTOKEN)){
                Log.d(TAG,"data.containsKey(AccountManager.KEY_AUTHTOKEN is true...");
                token = data.getString(AccountManager.KEY_AUTHTOKEN);
            }else{
                Log.d(TAG,"data.containsKey(AccountManager.KEY_AUTHTOKEN is false...");
            }

			String authType = data.getString(AccountManager.KEY_ACCOUNT_TYPE);
			Session.getUser().setAuthToken(authType, token);
			String fullName = data.getString(KEY_FULL_NAME);
			Session.getUser().setFullName(fullName);
			return TextUtils.isEmpty(token);
		}
		else{
            Log.d(TAG,"data.getBoolean(ServerAuthenticate.KEY_SUCCESS==False...");
			Session.getUser().invalidateAuthToken();
			return true;
		}
		
		
	}
	
}

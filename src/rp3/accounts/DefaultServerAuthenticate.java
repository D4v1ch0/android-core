package rp3.accounts;

import org.json.JSONObject;

import rp3.connection.WebService;
import rp3.runtime.Session;
import rp3.sync.TestConnection;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class DefaultServerAuthenticate implements ServerAuthenticate {
    private static final String TAG = DefaultServerAuthenticate.class.getSimpleName();
	public static String KEY_FULL_NAME = "Name";

	public Bundle signUp(final String name, final String email, final String pass, String authType){
		return null;
	}
	
    public Bundle signIn(final String user, final String pass, String authType) {
        Log.d(TAG,"signIn:"+user+" "+pass+" "+authType);
        WebService method = new WebService();
        Bundle bundle = new Bundle();
        if(TestConnection.executeSync()) {
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
                    authToken = response.getJSONObject("Data").getString("AuthToken");
                    fullName = response.getJSONObject("Data").getString("Name");
                    Session.getUser().setFullName(fullName);
                }

                if (!response.isNull("Message"))
                    bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, response.getJSONObject("Message").getString("Text"));

                bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, authType);
                bundle.putString(KEY_FULL_NAME, fullName);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, response.getJSONObject("Data").getBoolean("IsValid"));
            } catch (Exception e) {
                bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, e.getMessage());
                bundle.putString(AccountManager.KEY_AUTHTOKEN, null);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, false);
            }
        }
        else
        {
            if(Session.IsLogged())
            {
                //bundle.putString(AccountManager.KEY_AUTHTOKEN, Session.getUser().getAuthToken(authType));
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, authType);
                bundle.putString(KEY_FULL_NAME, Session.getUser().getFullName());
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, true);
            }
            else {
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
            String token = "temp";
            if(data.containsKey(AccountManager.KEY_AUTHTOKEN))
			    token = data.getString(AccountManager.KEY_AUTHTOKEN);
			String authType = data.getString(AccountManager.KEY_ACCOUNT_TYPE);
			Session.getUser().setAuthToken(authType, token);
			String fullName = data.getString(KEY_FULL_NAME);
			Session.getUser().setFullName(fullName);
			
			return TextUtils.isEmpty(token);
		}
		else{
			Session.getUser().invalidateAuthToken();
			return true;
		}
		
		
	}
	
}

package rp3.accounts;

import org.json.JSONObject;

import rp3.connection.WebService;
import rp3.runtime.Session;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.TextUtils;

public class DefaultServerAuthenticate implements ServerAuthenticate {

	public Bundle signUp(final String name, final String email, final String pass, String authType){
		return null;
	}
	
    public Bundle signIn(final String user, final String pass, String authType) {
    	
    	WebService method = new WebService();
    	method.setConfigurationName("Core", "SignIn");
    	method.setAuthTokenType(authType);    	    	
    	
    	method.addParameter("LogonName", user);
    	method.addParameter("Password", pass);
    	
    	Bundle bundle = new Bundle();
    		
    	
    	try {
			method.invokeWebService();
						
			JSONObject response = method.getJSONObjectResponse();
			String authToken = null;
			
			if(!response.getJSONObject("Data").isNull("AuthToken")){
				authToken = response.getJSONObject("Data").getString("AuthToken");
			}					
						
			if(!response.isNull("Message"))
				bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, response.getJSONObject("Message").getString("Text"));
			
			bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, authType);
			bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, response.getJSONObject("Data").getBoolean("IsValid"));			
    	}
    	catch(Exception e) {
    		bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, e.getMessage());
    		bundle.putString(AccountManager.KEY_AUTHTOKEN, null);
			bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, false);
    	}
		
    	return bundle;	       	    	
    }

	@Override
	public boolean requestSignIn() {		
		Bundle data = signIn(Session.getUser().getLogonName(), Session.getUser().getPassword(), User.getAccountType());
		
		String token = data.getString(AccountManager.KEY_AUTHTOKEN);
		String authType = data.getString(AccountManager.KEY_ACCOUNT_TYPE);
		
		Session.getUser().setAuthToken(authType, token);
		
		return TextUtils.isEmpty(token);
	}
	
}

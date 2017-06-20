package rp3.accounts;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import rp3.runtime.Session;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class Authenticator extends AbstractAccountAuthenticator {

    private static final String TAG = Authenticator.class.getSimpleName();
    private final Context mContext;

    private static ServerAuthenticate sServerAuthenticate;
    
    public Authenticator(Context context) {
        super(context);

        // I hate you! Google - set mContext as protected!
        this.mContext = context;
    }

    
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {        

        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    public static void setServerAuthenticate(ServerAuthenticate serverAuthenticate){
    	sServerAuthenticate = serverAuthenticate;
    }
    
    public static ServerAuthenticate getServerAuthenticate(){
    	if(sServerAuthenticate == null)
    		rp3.accounts.Authenticator.setServerAuthenticate(new DefaultServerAuthenticate());	
    	return sServerAuthenticate;
    }
    
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {      
//
//        // If the caller requested an authToken type we don't support, then
//        // return an error
//        if (!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY) && !authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
//            final Bundle result = new Bundle();
//            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
//            return result;
//        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
    	User user = User.getCurrentUser(mContext);
    	account = user.getAccount();
        final AccountManager am = AccountManager.get(mContext);
        
        String authToken = am.peekAuthToken(account, authTokenType);     
        
        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                try {
                    
                    Bundle r = sServerAuthenticate.signIn(account.name, password, authTokenType);
                    if(r.getBoolean(ServerAuthenticate.KEY_SUCCESS, false))      {              
                    	authToken = r.getString(ServerAuthenticate.KEY_AUTH_TOKEN);
                    	Session.getUser().setAuthToken(authTokenType, authToken);
                    }
                    else{
                    	//Toast.makeText(Session.getContext(), r.getInt(ServerAuthenticate.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = AuthenticatorActivity.newIntent(mContext, account.name, authTokenType, response);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Session.getContext().startActivity(intent);
        
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    @Override
    public String getAuthTokenLabel(String authTokenType) {       
            return authTokenType;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }


        
}

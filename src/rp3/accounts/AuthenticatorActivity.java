package rp3.accounts;


import rp3.accounts.User.UserUpdateCallback;
import rp3.configuration.Configuration;
import rp3.core.R;
import rp3.data.Constants;
import rp3.runtime.Session;
import rp3.util.ViewUtils;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
			
	public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String PARAM_USER_PASS = "USER_PASS";

    //private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();

    private String mAuthTokenType;
    
    public final static Intent newIntent(Context c){		
    	final Intent intent = new Intent(c, AuthenticatorActivity.class);                        
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_NAME, Session.getUser().getLogonName());
        
        return intent;
	}
    
    public static Intent newIntent(Context c, String accountName, 
    		String authTokenType, AccountAuthenticatorResponse response){
    	
    	final Intent intent = new Intent(c, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);        
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_NAME, accountName);
        
        return intent;
    }
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_authenticator);
        
        if(getActionBar()!=null)
        	getActionBar().hide();     
        
        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);            
        
        if (mAuthTokenType == null)
            mAuthTokenType = User.getAccountType();

        if (accountName != null) {
        	setLogonName(accountName);
        }

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(validate()){
            		submit(getLogonName(), getPassword());
            	}
            }
        });
        
        if(findViewById(R.id.button_help)!=null){
        	if(!rp3.configuration.Configuration.getAppConfiguration().allowHelpOnAut()){            	
        		findViewById(R.id.button_help).setVisibility(View.GONE);        		        	
        	}
        	
        	ViewUtils.setButtonClickListener(getWindow().getDecorView().getRootView(), R.id.button_help, new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					onHelpAction();
				}			
        	});
        }
        
        if(findViewById(R.id.button_settings)!=null){    		
    		
    		if(!rp3.configuration.Configuration.getAppConfiguration().allowSettingsOnAut()){
    			findViewById(R.id.button_settings).setVisibility(View.GONE);
    		}else if( Build.VERSION.SDK_INT >= 14 && ViewConfiguration.get(this).hasPermanentMenuKey()){  				
                  findViewById(R.id.button_settings).setVisibility(View.GONE);
    		}    		
    		
    		ViewUtils.setButtonClickListener(getWindow().getDecorView().getRootView(), R.id.button_settings, new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					onSettingsAction();
				}			
        	});
        }
        		
        
//        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Since there can only be one AuthenticatorActivity, we call the sign up activity, get his results,
//                // and return them in setAccountAuthenticatorResult(). See finishLogin().
//                Intent signup = new Intent(getBaseContext(), SignUpActivity.class);
//                signup.putExtras(getIntent().getExtras());
//                startActivityForResult(signup, REQ_SIGNUP);
//            }
//        });
    }

    public void onSettingsAction(){
    	callSettingsActivity();
    }
    
    public void onHelpAction(){
    	String url = rp3.configuration.Configuration.getAppConfiguration().getHelpUrl();
    	if(!TextUtils.isEmpty(url)){    	
    		if (!url.startsWith("http://") && !url.startsWith("https://"))
    			   url = "http://" + url;
    		
    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    		startActivity(browserIntent);
    	}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {		
    	if(rp3.configuration.Configuration.getAppConfiguration().allowSettingsOnAut())
    		getMenuInflater().inflate(R.menu.activity_authenticator, menu);
		
    	return super.onCreateOptionsMenu(menu);
	}        
    
    private void callSettingsActivity(){
    	Intent i = new Intent();
		i.setAction(Session.getContext().getApplicationContext().getPackageName() + ".SettingsActivity");
		startActivity(i);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    
    	if(item.getItemId() == R.id.action_settings){
    		callSettingsActivity();
    		return true;
    	}
    	
    	return super.onMenuItemSelected(featureId, item);
    }
    
    private String getLogonName(){
    	return ((TextView) findViewById(R.id.editText_user)).getText().toString();   	
    }
    
    private String getPassword(){
    	return ((TextView) findViewById(R.id.editText_password)).getText().toString();
    }
    
    private void setLogonName(String logonName){
    	((EditText) findViewById(R.id.editText_user)).setText(logonName);    	
    }
    
    private void setErrorUser(){
    	((EditText) findViewById(R.id.editText_user)).setError(getText(R.string.validation_field_required));
    }
    
    private void setErrorPassword(){
    	((EditText) findViewById(R.id.editText_password)).setError(getText(R.string.validation_field_required));
    }
    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        // The sign up activity returned that the user has successfully created an account
//        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
//            finishLogin(data);
//        } else
//            super.onActivityResult(requestCode, resultCode, data);
//    }        

    public boolean validate(){
    	String userName = getLogonName();
        String userPass = getPassword();

        boolean isValid = true;
        if(TextUtils.isEmpty(userName)){
        	setErrorUser();
        	isValid = false;
        }
        
        if(TextUtils.isEmpty(userPass)){
        	setErrorPassword();
        	isValid = false;
        }
        
        return isValid;
    }
    
    public void forgotPassword(View v)
    {
    	String url = Configuration.getAppConfiguration().get(Constants.KEY_FORGOT_PASSWORD);
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	i.setData(Uri.parse(url));
    	startActivity(i);
    }
    
    public void submit(final String logonName, final String password) {        
    	
    	final ProgressDialog progressDialog = ProgressDialog.show(this, getText(R.string.label_connecting), 
    			getText(R.string.message_please_wait), false);
        
    	new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {

                Log.d(TAG, "> Started authenticating");

                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    Bundle r = getServerAuthenticate().signIn(logonName, password, mAuthTokenType);

                    if(r.getBoolean(ServerAuthenticate.KEY_SUCCESS)){
                    	authtoken = r.getString(ServerAuthenticate.KEY_AUTH_TOKEN);
                    	
                    	data.putString(AccountManager.KEY_ACCOUNT_NAME, logonName);
                        data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                        data.putString(PARAM_USER_PASS, password);
                    }else{
                    	data.putString(KEY_ERROR_MESSAGE, r.getString(ServerAuthenticate.KEY_ERROR_MESSAGE));
                    }                                                            

                } catch (Exception e) {
                	Log.e(TAG, "Exception authenticating:" + e.getMessage());
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                	progressDialog.dismiss();                	
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();
    }

    private void finishLogin(final Intent intent) {

        User user = Session.getUser();        
        
        user.setLogonName(intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));        
        user.setPassword(intent.getStringExtra(PARAM_USER_PASS));
        user.isLogged(true);
        
        User.updateAccount(new UserUpdateCallback() {
			
			@Override
			public void onUserFinishUpdate(Bundle bundle) {
				String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
			    String authtokenType = mAuthTokenType;
			    			       
			    Session.getUser().setAuthToken(authtokenType, authtoken);
			    
			    setAccountAuthenticatorResult(intent.getExtras());
			    setResult(RESULT_OK, intent);
			    finish();
			}
		});                      
    }
    
    private ServerAuthenticate getServerAuthenticate(){
    	return Authenticator.getServerAuthenticate();
    }	
    
    @Override
    public void onBackPressed() {
    	setResult(RESULT_CANCELED);
    	super.onBackPressed();
    }
    
}

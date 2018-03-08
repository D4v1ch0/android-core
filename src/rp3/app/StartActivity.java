package rp3.app;

import rp3.accounts.AuthenticatorActivity;
import rp3.accounts.User;
import rp3.core.R;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class StartActivity extends BaseActivity {	

	private static final String TAG = StartActivity.class.getSimpleName()+"Base";
	public static final int REQUEST_LOGIN_ACTIVITY = 1;
	
	public static final String STATE_FIRST_LOAD = "firstload";
	
	private boolean firstTime = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		try{
			if(savedInstanceState!=null){
				Log.d(TAG,"savedInstanceState!=null...");
				firstTime = savedInstanceState.getBoolean(STATE_FIRST_LOAD);
			}

			Session.Start(this);
			setContentView(R.layout.activity_start);
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		Log.d(TAG,"...");
		outState.putBoolean(STATE_FIRST_LOAD, firstTime);
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		
		if(!ConnectionUtils.isNetAvailable(this) && Session.IsLogged())
		{
			Log.d(TAG,"!ConnectionUtils.isNetAvailable(this) && Session.IsLogged()");
			onContinue();
		}
		else if(firstTime){
			Log.d(TAG,"firstTime...");
			firstTime = false;
			if(Session.getUser().getAccount() == null){
				Log.d(TAG,"Session.getUser().getAccount() == null");
				callLoginActivity();
			}else{
				Log.d(TAG,"Session.getUser().getAccount() != null");
				if(rp3.runtime.Session.IsLogged()){
					Log.d(TAG,"rp3.runtime.Session.IsLogged()...");
                    onContinue();
				}else{
					callLoginActivity();Log.d(TAG,"!rp3.runtime.Session.IsLogged() No esta Logeado...");
				}
			}			
		}
	}
	
	public void callLoginActivity(){
		if(!onCallLoginActivity()){
			Log.d(TAG,"!onCallLoginActivity()...");
			Intent intent  = AuthenticatorActivity.newIntent(getApplicationContext(), Session.getUser().getLogonName(), 
					User.getAccountType(),
					null);
			Log.d(TAG,"REQUEST_LOGIN_ACTIVITY...");
			startActivityForResult(intent, REQUEST_LOGIN_ACTIVITY);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG,"onActivityResult...");
		switch(requestCode){
			case REQUEST_LOGIN_ACTIVITY:
				Log.d(TAG,"REQUEST_LOGIN_ACTIIVTY...");
				if(resultCode == RESULT_OK){
					onContinue();Log.d(TAG,"resultCode == RESULT_OK...");
				}
				else if(resultCode == RESULT_CANCELED){
					finish();Log.d(TAG,"resultCode == RESULT_CANCELED...");
				}
				else{
					callLoginActivity();Log.d(TAG,"resultCode == OTHER CODE...");
				}
				break;
		}
	}
	
	public boolean onCallLoginActivity(){
		Log.d(TAG,"onCallLoginActivity...");
		return false;
	}		
	
	public void onVerifyRequestSignIn(){
		
		new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {

                return rp3.accounts.Authenticator.getServerAuthenticate().requestSignIn();
            }

            @Override
            protected void onPostExecute(Boolean response) {
            	if(response)
            		callLoginActivity();
            	else
            		onContinue();
            }
        }.execute();        
	}
		
	public void onContinue(){
		Log.d(TAG,"onContinue desde el starApp...");
	}	
			
	public void setImage(int resID){
		Log.d(TAG,"setImage...");
		ImageView img = (ImageView)this.findViewById(R.id.imageView_icon);	
		img.setImageResource(resID);
	}
	
}

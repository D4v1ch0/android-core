package rp3.app;

import rp3.accounts.AuthenticatorActivity;
import rp3.accounts.User;
import rp3.core.R;
import rp3.runtime.Session;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class StartActivity extends BaseActivity {	
		
	public static final int REQUEST_LOGIN_ACTIVITY = 1;	
	private boolean firstTime = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);						
		Session.Start(this.getApplicationContext());
		setContentView(R.layout.activity_start);				
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		
		if(firstTime){
			firstTime = false;			
			
			if(Session.getUser().getAccount() == null){
				callLoginActivity();
			}else{
				if(rp3.runtime.Session.IsLogged()){			
					onVerifyRequestSignIn();
				}else
					callLoginActivity();
			}			
		}
	}
	
	public void callLoginActivity(){
		if(!onCallLoginActivity()){
			Intent intent  = AuthenticatorActivity.newIntent(getApplicationContext(), Session.getUser().getLogonName(), 
					User.DEFAULT_TOKEN_TYPE , 
					null);			
			startActivityForResult(intent, REQUEST_LOGIN_ACTIVITY);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case REQUEST_LOGIN_ACTIVITY:
				
				if(resultCode == RESULT_OK)
					onContinue();
				else if(resultCode == RESULT_CANCELED)
					finish();
				else
					callLoginActivity();
					
				break;
		}
	}
	
	public boolean onCallLoginActivity(){
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
	}	
			
	public void setImage(int resID){
		ImageView img = (ImageView)this.findViewById(R.id.imageView_icon);	
		img.setImageResource(resID);
	}
	
}

package rp3.app;

import rp3.accounts.AuthenticatorActivity;
import rp3.accounts.User;
import rp3.core.R;
import rp3.runtime.Session;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class StartActivity extends BaseActivity {	
		
	public static final int REQUEST_LOGIN_ACTIVITY = 1;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
				
		setContentView(R.layout.activity_start);
		
		Session.Start(this.getApplicationContext());
		
		if(Session.getUser().getAccount() == null){
			callLoginActivity();
		}else{
			if(rp3.runtime.Session.IsLogged()){			
				if(onNeedRequestAut()){
					callLoginActivity();
				}
				else{
					onContinue();
				}			
			}else
				callLoginActivity();
		}
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		
					
	}
	
	private void callLoginActivity(){
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
	
	public boolean onNeedRequestAut(){
		return true;
	}

	public void onContinue(){
	}
	
			
	public void setImage(int resID){
		ImageView img = (ImageView)this.findViewById(R.id.imageView_icon);	
		img.setImageResource(resID);
	}
	
}

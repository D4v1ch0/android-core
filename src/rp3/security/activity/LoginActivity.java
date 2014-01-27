package rp3.security.activity;

import rp3.runtime.Session;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	SharedPreferences preferences;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(rp3.core.R.layout.activity_login);                     
                
        getActionBar().hide();
        
        Session.Start(this);
        
        setUser(Session.User.getUser());
        
        Button buttonLogin = (Button)findViewById(rp3.core.R.id.button_login);        
        
        buttonLogin.setOnClickListener(this);        
    }       
    
    private String getUser()
    {    	
    	return  ((EditText)findViewById(rp3.core.R.id.editText_user)).getText().toString();
    }
    
    private void setUser(String userId)
    {
    	((EditText)findViewById(rp3.core.R.id.editText_user)).setText(userId);
    }
    
	private String getPassword()
    {
    	return ((EditText)findViewById(rp3.core.R.id.editText_user)).getText().toString();
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == rp3.core.R.id.button_login)
		{		
			if(Session.Start(this, getUser(), getPassword()))
			{
				this.setResult(RESULT_OK);
				this.finish();
				//finishActivity(Constants.LOGIN_REQUEST_CODE);				
			}
			else
				Toast.makeText(this, "Invalid " + getUser(), Toast.LENGTH_SHORT).show();
		}/*
		else if(v.getId() == R.id.buttonCancel)
		{
			this.setResult(RESULT_CANCELED);
			finishActivity(Constants.LOGIN_REQUEST_CODE);
		}*/
	}
}
package rp3.app;

import rp3.accounts.AuthenticatorActivity;
import rp3.accounts.User;
import rp3.configuration.PreferenceManager;
import rp3.core.R;
import rp3.data.Constants;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends BaseActivity {

	private static final String TAG = StartActivity.class.getSimpleName()+"Base";
	public static final int REQUEST_LOGIN_ACTIVITY = 1;

	public static final String STATE_FIRST_LOAD = "firstload1";

	private boolean firstTime = true;

	private List<String> permissionsAll;
	private static final int MY_PERMISSIONS=100;

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
		Log.d(TAG,"onSaveInstanceState...");
		//outState.putBoolean(STATE_FIRST_LOAD, firstTime);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(!ConnectionUtils.isNetAvailable(this) && Session.IsLogged())
		{
			Log.d(TAG,"!ConnectionUtils.isNetAvailable(this) && Session.IsLogged()");
			onContinue();
		}else if(firstTime){
			firstTime = false;
			Log.d(TAG,"firsTime...");
			if(Session.getUser().getAccount() == null){
				Log.d(TAG,"getAccount == null...");
				callLoginActivity();
			}else{
				Log.d(TAG,"getAccount != null...");
				Log.d(TAG,"Session:"+Session.getUser().toString());
				System.out.print(Session.getUser());
				//if(Session.getUser().isLogged()){
				String session = PreferenceManager.getString(Constants.KEY_LOGIN_SESSION,"");
				Log.d(TAG,"Esta logeado:"+session);
				if(session.equalsIgnoreCase("true")){
					Log.d(TAG,"Session is Logged...");
					Log.d(TAG,"Usuario Logeado:"+Session.getUser().toString());
					String logonName = PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"");
					String passowrd = PreferenceManager.getString(Constants.KEY_LAST_PASS,"");
					Log.d(TAG,"LogonName:"+logonName);
					Log.d(TAG,"Password:"+passowrd);
					if(PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"").equalsIgnoreCase(Session.getUser().getLogonName()) &&
							PreferenceManager.getString(Constants.KEY_LAST_PASS,"").equalsIgnoreCase(Session.getUser().getPassword())){
						Log.d(TAG,"Usuario y clave temporales son iguales al logeado...");
						onContinue();
					}else{
						Log.d(TAG,"Usuari o y clave temporalres no son iguales al logeado...");
						callLoginActivity();
					}
				}else{
					Log.d(TAG,"Session No is Logged...");
					callLoginActivity();
				}

			}
		}
		/*else if(firstTime){
			Log.d(TAG,"firstTime...");
			firstTime = false;
			if(Session.getUser().getAccount() == null){
				Log.d(TAG,"Session.getUser().getAccount() == null");
				callLoginActivity();
			}else if(Session.IsLogged()==false){
				Log.d(TAG,"Session.IsLogged()==false...");
				callLoginActivity();
			}else{
				Log.d(TAG,"Session.getUser().getAccount() != null");
				//if(rp3.runtime.Session.IsLogged()){
					if(Session.IsLogged()){
						onContinue();
					}else{
						callLoginActivity();
					}
			}			
		}else{
			if(Session.getUser().getAccount() == null){
				Log.d(TAG,"Session.getUser().getAccount() == null");
				callLoginActivity();
			}else if(Session.IsLogged()==false){
				Log.d(TAG,"Session.IsLogged()==false...");
				callLoginActivity();
			}else{
				Log.d(TAG,"Session.getUser().getAccount() != null");
				//if(rp3.runtime.Session.IsLogged()){
				if(Session.IsLogged()){
					onContinue();
				}else{
					callLoginActivity();
				}
			}
		}*/
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
					onContinue();
					Log.d(TAG,"resultCode == RESULT_OK...");
					//PreferenceManager.setValue("LOGIN_RP3_RETAIL",true);
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

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
		validatePermissionApp();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode==MY_PERMISSIONS) {
			if (grantResults.length > 0) {
				Log.d(TAG,"grantResults > 0");
				if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED &&
						grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED
						&& grantResults[4]==PackageManager.PERMISSION_GRANTED){
					Log.d(TAG,"All grant result Aceptados...");
					validatePermissionApp();
				}
				else {
					Log.d(TAG,"Algun grant result no ha sido aceptado...");
					validatePermissionApp();
				}
			}
			else {
				Log.d(TAG,"no hay grant results...");
				validatePermissionApp();
			}
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


	private void validatePermissionApp() {
		int fineCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		int fineStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
		int fineContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
		int fineTelephonyPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
		//int fineAccountManagerPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCOUNT_MANAGER);

		permissionsAll = new ArrayList<>();
		permissionsAll.add(Manifest.permission.CAMERA);
		permissionsAll.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		permissionsAll.add(Manifest.permission.ACCESS_FINE_LOCATION);
		permissionsAll.add(Manifest.permission.GET_ACCOUNTS);
		permissionsAll.add(Manifest.permission.CALL_PHONE);
		permissionsAll.add(Manifest.permission.ACCOUNT_MANAGER);


		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
			Log.d(TAG,"version >Mr1 permissions runtime required...");
			if (fineStoragePermission != PackageManager.PERMISSION_GRANTED || fineCameraPermission != PackageManager.PERMISSION_GRANTED
					|| fineLocationPermission != PackageManager.PERMISSION_GRANTED ||fineContactPermission != PackageManager.PERMISSION_GRANTED
					|| fineTelephonyPermission != PackageManager.PERMISSION_GRANTED ) {
				ActivityCompat.requestPermissions(this, permissionsAll.toArray(new String[permissionsAll.size()]), MY_PERMISSIONS);
				Log.d(TAG,"Algun permiso falto ser aprobado...");
				return;
			}
			else{
				Log.d(TAG,"Todos los permisos han sido aceptados por el usuario...");
			}
		}
		else {
			Log.d(TAG,"Todos los permisos han sido aceptados porque es una version < Lollipop_Mr1...");
		}
	}

	//region Ciclo de vida


	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG,"onBackPressed...");
	}

	//endregion
}

package rp3.accounts;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import rp3.runtime.Session;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class User {
	private static final String TAG = "UserAccountSession";
	public static final String USER_DATA_USERID = "userid";
	public static final String USER_DATA_ISLOGGED = "islogged";
	public static final String USER_DATA_FULLNAME = "fullName";

	public static final String DEFAULT_TOKEN_TYPE = "defaultTokenType";

	private AccountManager accountManager;
	
	private int userId;
	private String logonName;
	private String fullName;
	private String password;
	private boolean logged;	
	
	private static String accountType;
	private static String authority;

	private boolean current;
	private Account account;
	private Context context;
	
	User(Context c) {
		context = c;
		accountManager = AccountManager.get(context);
	}

	// @Override
	// public long getID() {
	// return this.userId;
	// }
	//
	// @Override
	// public void setID(long id) {
	// this.userId = (int)id;
	// }

	public Account getAccount() {
		return account;
	}

	void setAccount(Account account) {
		this.account = account;
	}

	int getUserId() {
		return userId;
	}

	public final void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLogonName() {
		return logonName;
	}

	public String getPassword() {
		return password;			
	}

	void setPassword(String password) {
		this.password = password;	
	}	

	void setLogonName(String logonName) {
		this.logonName = logonName;
	}

	public boolean isLogged() {
		return logged;
	}

	public void isLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isCurrent() {
		return current;
	}

	void isCurrent(boolean current) {
		this.current = current;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public static String getAccountType() {
		if (accountType == null)
			initializeAuthenticatorParams(Session.getContext());
		return accountType;
	}
	
	public String getAuthTokenType() {
		return getAccountType();
	}
	
	public static String getAuthority() {
		if (authority == null)
			initializeAuthenticatorParams(Session.getContext());
		return authority;
	}		

	private static void initializeAuthenticatorParams(Context c) {
		int resourceAuthId = c.getResources().getIdentifier("authenticator", "xml",
				c.getApplicationContext().getPackageName());
		XmlResourceParser parserAuth = c.getResources().getXml(resourceAuthId);

		int resourceSync = c.getResources().getIdentifier("syncadapter", "xml",
				c.getApplicationContext().getPackageName());
		XmlResourceParser parserSync = c.getResources().getXml(resourceSync);
		
		try {
			parserAuth.next();
			parserAuth.next();
			
			accountType = parserAuth
					.getAttributeValue(
							"http://schemas.android.com/apk/res/android",
							"accountType");
			
			parserSync.next();
			parserSync.next();
			
			authority = parserSync
					.getAttributeValue(
							"http://schemas.android.com/apk/res/android",
							"contentAuthority");
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	// @Override
	// public boolean isAutoGeneratedId() {
	// return true;
	// }
	//
	// @Override
	// public String getTableName() {
	// return Contract.User.TABLE_NAME;
	// }
	//
	// @Override
	// public void setValues() {
	// setValue(Contract.User._ID, this.userId);
	// setValue(Contract.User.COLUMN_CURRENT, this.current);
	// setValue(Contract.User.COLUMN_FULLNAME, this.fullName);
	// setValue(Contract.User.COLUMN_LOGONNAME, this.logonName);
	// setValue(Contract.User.COLUMN_ISLOGGED, this.logged);
	// }

	public static User getCurrentUser(Context c) {
		User user = new User(c);
		
		Account account = getDefaultAccount(c);

		if (account != null) {			
			user.setAccount(account);
			user.setLogonName(account.name);
			
			AccountManager mAccountManager = AccountManager.get( Session.getContext()  );
			user.setPassword(mAccountManager.getPassword(account));
			
			String userId = user.getUserData(account,USER_DATA_USERID);
			if(!TextUtils.isEmpty(userId))			
				user.setUserId(Integer.parseInt(userId));
			else
				user.setUserId(0);
			
			user.setFullName(user.getUserData(account, USER_DATA_FULLNAME));
			
			String isLogged = user.getUserData(account,USER_DATA_ISLOGGED);
			if(!TextUtils.isEmpty(isLogged))
				user.isLogged(Boolean.parseBoolean(isLogged));
			else
				user.isLogged(false);
		}

		// Cursor c = db.query(Contract.User.TABLE_NAME, new String[]{
		// Contract.User._ID,
		// Contract.User.COLUMN_FULLNAME,
		// Contract.User.COLUMN_ISLOGGED,
		// Contract.User.COLUMN_CURRENT,
		// Contract.User.COLUMN_LOGONNAME
		// }, Contract.User.COLUMN_CURRENT + "=?", true);
		//
		// if(c.moveToNext()){
		// user = new User();
		// user.setUserId(CursorUtils.getInt(c,Contract.User._ID));
		// user.setFullName(CursorUtils.getString(c,Contract.User.FIELD_FULLNAME));
		// user.setLogonName(CursorUtils.getString(c,Contract.User.COLUMN_LOGONNAME));
		// user.setUserId(CursorUtils.getInt(c,Contract.User._ID));
		// user.isCurrent(CursorUtils.getBoolean(c,Contract.User.FIELD_CURRENT));
		// }
		return user;
	}
	
	public static void updateAccount(final UserUpdateCallback userUpdateCallback){
		final User user = Session.getUser();
						
		String accountName = user.getLogonName();

        Account account = user.getAccount();
        final AccountManager mAccountManager = AccountManager.get( Session.getContext()  );
       
        final boolean addingAccount = account == null;
        final boolean deleteAccount = account != null && !account.name.equals(accountName);
        final boolean updateAccount = !deleteAccount;                
        	
        AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>(){
            @Override
            public void run(AccountManagerFuture<Boolean> arg0){
            	
            	if(deleteAccount){
            		finishUserUpdate(user, mAccountManager, true);
            	}
            	if(userUpdateCallback!=null)
            		userUpdateCallback.onUserFinishUpdate(null);
            }
        };
        
       
        if(deleteAccount){
        	Log.d(TAG,"Eliminar account...");
        	mAccountManager.removeAccount(account, callback, null);
        }else if(addingAccount){
        	Log.d(TAG,"Agregar account...");
        	finishUserUpdate(user, mAccountManager, true);
        	if(userUpdateCallback!=null){
        		Log.d(TAG,"userUpdateCallback!=null...");
				userUpdateCallback.onUserFinishUpdate(null);
			}else{
				Log.d(TAG,"userUpdateCallback==null...");
			}

        }else if(updateAccount){
        	Log.d(TAG,"Update account...");
        	finishUserUpdate(user, mAccountManager, false); 
        	if(userUpdateCallback!=null)
        		userUpdateCallback.onUserFinishUpdate(null);
        }else{

		}
    }

	private static void finishUserUpdate(User user, AccountManager mAccountManager, boolean addAccount){		
		if(addAccount){
			user.setAccount(new Account(user.getLogonName(), User.getAccountType()));
            mAccountManager.addAccountExplicitly(user.getAccount(), user.getPassword(), null);
		}else{
			mAccountManager.setPassword(user.getAccount(), user.getPassword());			
		}
		Account account = user.getAccount();
				
		mAccountManager.setUserData(account, USER_DATA_FULLNAME, user.getFullName());
        mAccountManager.setUserData(account, USER_DATA_USERID, String.valueOf(user.getUserId()) );
        mAccountManager.setUserData(account, USER_DATA_ISLOGGED, String.valueOf(user.isLogged()) );        
	}
	
	public static Account getDefaultAccount(Context c) {
		final Account availableAccounts[] = AccountManager.get(c)
				.getAccountsByType(getAccountType());
		if (availableAccounts.length > 0)
			return availableAccounts[0];
		else
			return null;
	}

	public String getUserData(Account account, String key) {
		return accountManager.getUserData(account, key);
	}

	public void invalidateAuthToken() {
		AccountManager mAccountManager = AccountManager.get( Session.getContext()  );
		mAccountManager.invalidateAuthToken(Session.getUser().getAuthTokenType(), Session.getUser().getAuthToken());
	}
	
	public String getAuthToken() {
		return getAuthToken((context instanceof Activity? (Activity)context: null), getAccountType());
	}
	
	public String getAuthToken(String authTokenType)  {
		return getAuthToken((context instanceof Activity? (Activity)context: null), authTokenType);
	}
	
	public String getAuthToken(Activity activity)  {		
		return getAuthToken(activity, User.getAccountType());
	}
	
	public String getAuthToken(Activity activity, String authTokenType) {
		final AccountManagerFuture<Bundle> future = 
				accountManager.getAuthToken(account, authTokenType, null, activity, null, null);
		
		Bundle bnd = null;
		try {
			bnd = future.getResult();
		} catch (OperationCanceledException e) {
			Log.e("User Token:","OperationCanceledException:"+ e.getMessage());
		} catch (AuthenticatorException e) {
			Log.e("User Token: ","AuthenticatorException:" + e.getMessage());
		} catch (IOException e) {
			Log.e("User Token:","IOException:"+ e.getMessage());
		}

		final String authtoken = bnd
				.getString(AccountManager.KEY_AUTHTOKEN);				
		Log.d("User Token:","AuthToken:"+authtoken);
		return authtoken;
	}
	
	public void setAuthToken(String authtokenType, String authtoken){
		AccountManager mAccountManager = AccountManager.get( Session.getContext()  );
		Account account = Session.getUser().getAccount();
				
	    mAccountManager.setAuthToken(account, authtokenType, authtoken);
	}
	
	public interface UserUpdateCallback
	{
		public void onUserFinishUpdate(Bundle bundle);
	}


	@Override
	public String toString() {
		return "User{" +
				"accountManager=" + accountManager +
				", userId=" + userId +
				", logonName='" + logonName + '\'' +
				", fullName='" + fullName + '\'' +
				", password='" + password + '\'' +
				", logged=" + logged +
				", current=" + current +
				", account=" + account +
				", context=" + context +
				'}';
	}
}

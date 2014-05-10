package rp3.accounts;

import android.os.Bundle;

public interface ServerAuthenticate {
	
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_AUTH_TOKEN = "authtoken";
	public static final String KEY_HAS_ERROR = "haserror";
	public static final String KEY_ERROR_MESSAGE = "errormessage";
	
	public boolean requestSignIn();
    public Bundle signUp(final String name, final String email, final String pass, String authType) throws Exception;
    public Bundle signIn(final String user, final String pass, String authType) throws Exception;
}

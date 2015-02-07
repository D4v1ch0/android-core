package rp3.sync;

import rp3.accounts.User;
import rp3.runtime.Session;
import android.content.ContentResolver;
import android.os.Bundle;

public class SyncUtils {		
	
	public static void requestSync(Bundle settingsBundle){
		// Pass the settings flags by inserting them in a bundle
		if(settingsBundle == null)
			settingsBundle = new Bundle();
        
		settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        
		/*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(Session.getUser().getAccount(), User.getAuthority(), settingsBundle);
	}
	
}

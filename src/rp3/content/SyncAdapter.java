package rp3.content;

import rp3.configuration.PreferenceManager;
import rp3.core.R;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.runtime.Session;
import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

public abstract class SyncAdapter extends AbstractThreadedSyncAdapter {
    
	public static final String TAG = "SyncAdapter";
	
	public static final String ARG_SYNC_TYPE = "ARG_SYNC_TYPE";
	
	public static final String SYNC_FINISHED = "SYNC_FINISHED";
	
	public static final String NOTIFY_EXTRA_MESSAGES = "NOTIFY_EXTRA_MESSAGES";
	public static final String NOTIFY_EXTRA_DATA = "NOTIFY_EXTRA_DATA";
			
	public static final int SYNC_EVENT_SUCCESS = 0;
	public static final int SYNC_EVENT_ERROR = 1;	
	public static final int SYNC_EVENT_CONNECTION_FAILED = 2;	
	public static final int SYNC_EVENT_PARTIAL = 3;
	public static final int SYNC_EVENT_HTTP_ERROR = 4;
	public static final int SYNC_EVENT_AUTH_ERROR = 5;
	
	private MessageCollection messages = new MessageCollection();
	private Bundle data = new Bundle();
	
	/**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);        
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);        
    }
    
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		messages.clear();		
		if(extras != null)
			data = extras;
		else
			data = new Bundle();
			
		PreferenceManager.close();
		Session.Start(this.getContext());
		rp3.configuration.Configuration.TryInitializeConfiguration(this.getContext());		
	}
			
	
	public void notifySyncFinish(){
		Intent i = new Intent(SYNC_FINISHED);				
		i.putExtra(NOTIFY_EXTRA_MESSAGES, messages);
		i.putExtra(NOTIFY_EXTRA_DATA, data);
		getContext().sendBroadcast(i);
	}
	//proof
	public void putData(String key, String value){
		data.putString(key, value);
	}
	
	public void putData(String key, int value){
		data.putInt(key, value);
	}
	
	public void putData(String key, long value){
		data.putLong(key, value);
	}
	
	public void putData(String key, float value){
		data.putFloat(key, value);
	}
	
	public void putData(String key, boolean value){
		data.putBoolean(key, value);
	}	
	
	public void addMessage(Message message){
		messages.addMessage(message);
	}
	
	public void addErrorMessage(String text){
		messages.addErrorMessage(text);
	}
	
	public void addWarningMessage(String text){
		messages.addWarningMessage(text);
	}
	
	public void addMessage(String text, int type){
		messages.addMessage(text, type);
	}		
	
	public void addDefaultMessage(int syncEvent){
		switch (syncEvent) {
		case SYNC_EVENT_CONNECTION_FAILED:
			messages.addErrorMessage(getContext().getText(R.string.message_error_sync_connection_server_fail).toString());
			break;	
		case SYNC_EVENT_HTTP_ERROR:
			messages.addErrorMessage(getContext().getText(R.string.message_error_sync_connection_http_error).toString());
			break;
		case SYNC_EVENT_AUTH_ERROR:
			break;
		case SYNC_EVENT_SUCCESS:
			break;
		default:
			messages.addErrorMessage(getContext().getText(R.string.message_error_sync_general_error).toString());
			break;
		}
	}
    
}

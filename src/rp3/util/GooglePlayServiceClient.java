package rp3.util;

import rp3.app.BaseActivity;
import rp3.content.SimpleCallback;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class GooglePlayServiceClient implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

	BaseActivity activity;
	SimpleCallback connectedCallback;
	
	public GooglePlayServiceClient(BaseActivity activity, SimpleCallback connectedCallback){
		this.activity = activity;
		this.connectedCallback = connectedCallback;
	}
	
	public GooglePlayServiceClient(BaseActivity activity){
		this.activity = activity;
	}
	
	public GooglePlayServiceClient(SimpleCallback connectedCallback){
		this.connectedCallback = connectedCallback;
	}
	
	public GooglePlayServiceClient(){
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if(activity==null) return;		
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        activity,
                        GooglePlayServicesUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            GooglePlayServicesUtils.showErrorDialog(activity,connectionResult.getErrorCode());
        }
	}

	@Override
	public void onConnected(Bundle bundle) {
		if(connectedCallback!=null)
			connectedCallback.onExecute(bundle);		
	}

    @Override
    public void onConnectionSuspended(int i) {

    }

}

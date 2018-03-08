package rp3.util;

import rp3.app.BaseActivity;
import rp3.runtime.Session;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServicesUtils {	
    
	// Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    public final static String TAG_DIALOG_NAME = "GooglePlayServicesError";
    
    
	 // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class GoogleErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        private AppCompatActivity appCompatActivity;
        // Default constructor. Sets the dialog field to null
        public GoogleErrorDialogFragment(AppCompatActivity activity) {
            super();
            mDialog = null;
            appCompatActivity = activity;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    googleServicesConnected(appCompatActivity);
                }
            });
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public static void showErrorDialog(BaseActivity activity, int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode,
            activity,
            GooglePlayServicesUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(activity.getSupportFragmentManager(), TAG_DIALOG_NAME);
        }
    }
    
    public static boolean servicesConnected(BaseActivity activity) {    	 
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(Session.getContext());
        
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
        	
        	showErrorDialog(activity, resultCode);
            return false;
        }
    }

    public static boolean googleServicesConnected(AppCompatActivity activity) {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(Session.getContext());

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("GooglePlayServicesUtils",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {

            googleShowErrorDialog(activity, resultCode);
            return false;
        }
    }

    public static void googleShowErrorDialog(AppCompatActivity activity, int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                activity,
                GooglePlayServicesUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            GoogleErrorDialogFragment errorFragment = new GoogleErrorDialogFragment(activity);

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            errorFragment.setCancelable(false);
            // Show the error dialog in the DialogFragment
            errorFragment.show(activity.getSupportFragmentManager(), TAG_DIALOG_NAME);
        }
    }
}

package rp3.util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseActivity;
import rp3.content.SimpleCallback;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.LocationClient;

public class LocationUtils {

	public static LocationClient getLocationClient(BaseActivity activity){
		GooglePlayServiceClient client = new GooglePlayServiceClient(activity);
		LocationClient locationClient = new LocationClient(activity, client, client);
		return locationClient;
	}
    
	public static String getAddress(Context c, Location location){
		Geocoder geocoder =
                new Geocoder(c, Locale.getDefault());
        // Get the current location from the input parameter list
        Location loc = location;
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
            /*
             * Return 1 address.
             */
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e1) {
        Log.e("LocationSampleActivity",
                "IO Exception in getFromLocation()");        
        return "";
        } catch (IllegalArgumentException e2) {
        // Error message to post in the log
        String errorString = "Illegal arguments " +
                Double.toString(loc.getLatitude()) +
                " , " +
                Double.toString(loc.getLongitude()) +
                " passed to address service";
        Log.e("LocationSampleActivity", errorString);        
        return "";
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
            /*
             * Format the first line of address (if available),
             * city, and country name.
             */
            String addressText = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());
            // Return the text
            return addressText;
        } else {
            return "";
        }
	}
	
	public static void getAddressAsync(final Context c, Location location, final SimpleCallback callback){
				
		new AsyncTask<Location, Integer, String>() {

			@Override
			protected String doInBackground(Location... params) {
				try{
					return getAddress(c, params[0]);
				}catch(Exception e){
					return e.getMessage();
				}
			}
			
			@Override
			protected void onPostExecute(String result) {				
				callback.onExecute(result);
			}
		}.execute(location);		
	}
	
    
}
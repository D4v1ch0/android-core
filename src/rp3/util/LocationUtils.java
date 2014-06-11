package rp3.util;

import rp3.app.BaseActivity;

import com.google.android.gms.location.LocationClient;

public class LocationUtils {

	public static LocationClient getLocationClient(BaseActivity activity){
		GooglePlayServiceClient client = new GooglePlayServiceClient(activity);
		LocationClient locationClient = new LocationClient(activity, client, client);
		return locationClient;
	}
    
    
}

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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

public class LocationUtils {

    private static final float ACCURACY = 25;
    private static final int TIMES = 5;
	public interface OnLocationResultListener {
		void getLocationResult(Location location);
	}

	public static GoogleApiClient getLocationClient(BaseActivity activity) {
		GooglePlayServiceClient client = new GooglePlayServiceClient(activity);
        GoogleApiClient locationClient = new GoogleApiClient.Builder(activity).addApi(Drive.API).build();
		return locationClient;
	}

	public static GoogleApiClient getLocationClient(Context c) {
		GooglePlayServiceClient client = new GooglePlayServiceClient();
        GoogleApiClient locationClient = new GoogleApiClient.Builder(c).addApi(Drive.API).build();
		return locationClient;
	}
	
	public static Location getLocation(Context c) {
		Location location = null;
        try {        	
            LocationManager locationManager = (LocationManager) c
                    .getSystemService(Context.LOCATION_SERVICE);
 
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
               
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0,
                            0, new LocationListener() {
								
								@Override
								public void onStatusChanged(String provider, int status, Bundle extras) {									
								}
								
								@Override
								public void onProviderEnabled(String provider) {									
								}
								
								@Override
								public void onProviderDisabled(String provider) {									
								}
								
								@Override
								public void onLocationChanged(Location location) {									
								}
							});
                    
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);                        
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                0,
                                0, new LocationListener() {
									
									@Override
									public void onStatusChanged(String provider, int status, Bundle extras) {									
									}
									
									@Override
									public void onProviderEnabled(String provider) {										
									}
									
									@Override
									public void onProviderDisabled(String provider) {										
									}
									
									@Override
									public void onLocationChanged(Location location) {
									}
								});
                        
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);                           
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
		
    }
   
//	public static void getLocationAsync(Context c, final OnLocationResultListener resultCallback){
//		LocationClient locationClient = new LocationClient(c,new ConnectionCallbacks() {
//			
//			@Override
//			public void onDisconnected() {
//			}
//			
//			@Override
//			public void onConnected(Bundle arg0) {
//				resultCallback.getLocationResult(LocationClient.this.getLastLocation());
//			}
//		},
//			new OnConnectionFailedListener() {			
//			@Override
//			public void onConnectionFailed(ConnectionResult arg0) {
//				resultCallback.getLocationResult(null);
//			}
//		});		
//	}

	public static void getLocation(final Context c, final OnLocationResultListener callback) {
		Location location = null;
		try {
			final LocationManager locationManager = (LocationManager) c
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				callback.getLocationResult(null);
			} else {

				LocationListener listener = new LocationListener() {
                    private int times = 0;
                    private Location last = null;
					@Override
					public void onStatusChanged(
							String provider, int status,
							Bundle extras) {
					}

					@Override
					public void onProviderEnabled(
							String provider) {
					}

					@Override
					public void onProviderDisabled(
							String provider) {
					}

					@Override
					public void onLocationChanged(
							Location location) {
                        //Toast.makeText(c, "Tiempo: " + times + " Precision: " + location.getAccuracy() + " Latitud:" + location.getLatitude() + " Longitud:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        if(location.getAccuracy() < ACCURACY) {
                            callback.getLocationResult(location);
                            locationManager.removeUpdates(this);
                        }
                        else {
                            if(last == null || last.getAccuracy() > location.getAccuracy())
                            {
                                last = location;
                            }
                            if (times == TIMES) {
                                callback.getLocationResult(last);
                                locationManager.removeUpdates(this);
                            }
                        }
                        times++;
					}
				};				
				
				// First get location from GPS
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {

					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 1000, 0,listener);

//					if (locationManager != null) {
//						location = locationManager
//								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//												
//					}

				}

				// First get location from Network Provider
				if (isNetworkEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER, 1000, 0,listener);

//						if (locationManager != null) {
//							location = locationManager
//									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	// public static void getLocationAsync(Context c, final
	// OnLocationResultListener resultCallback){
	// LocationClient locationClient = new LocationClient(c,new
	// ConnectionCallbacks() {
	//
	// @Override
	// public void onDisconnected() {
	// }
	//
	// @Override
	// public void onConnected(Bundle arg0) {
	// resultCallback.getLocationResult(LocationClient.this.getLastLocation());
	// }
	// },
	// new OnConnectionFailedListener() {
	// @Override
	// public void onConnectionFailed(ConnectionResult arg0) {
	// resultCallback.getLocationResult(null);
	// }
	// });
	// }

	public static String getAddress(Context c, Location location) {
		Geocoder geocoder = new Geocoder(c, Locale.getDefault());
		// Get the current location from the input parameter list
		Location loc = location;
		// Create a list to contain the result address
		List<Address> addresses = null;
		try {
			/*
			 * Return 1 address.
			 */
			addresses = geocoder.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
		} catch (IOException e1) {
			Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
			return "";
		} catch (IllegalArgumentException e2) {
			// Error message to post in the log
			String errorString = "Illegal arguments "
					+ Double.toString(loc.getLatitude()) + " , "
					+ Double.toString(loc.getLongitude())
					+ " passed to address service";
			Log.e("LocationSampleActivity", errorString);
			return "";
		}
		// If the reverse geocode returned an address
		if (addresses != null && addresses.size() > 0) {
			// Get the first address
			Address address = addresses.get(0);
			/*
			 * Format the first line of address (if available), city, and
			 * country name.
			 */
			String addressText = String.format(
					"%s, %s, %s",
					// If there's a street address, add it
					address.getMaxAddressLineIndex() > 0 ? address
							.getAddressLine(0) : "",
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

	public static void getAddressAsync(final Context c, Location location,
			final SimpleCallback callback) {

		new AsyncTask<Location, Integer, String>() {

			@Override
			protected String doInBackground(Location... params) {
				try {
					return getAddress(c, params[0]);
				} catch (Exception e) {
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

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

import com.google.android.gms.location.LocationClient;

public class LocationUtils {

	public interface OnLocationResultListener {
		void getLocationResult(Location location);
	}

	public static LocationClient getLocationClient(BaseActivity activity) {
		GooglePlayServiceClient client = new GooglePlayServiceClient(activity);
		LocationClient locationClient = new LocationClient(activity, client,
				client);
		return locationClient;
	}

	public static LocationClient getLocationClient(Context c) {
		GooglePlayServiceClient client = new GooglePlayServiceClient();
		LocationClient locationClient = new LocationClient(c, client, client);
		return locationClient;
	}
	
	public static Location getLocation(Context c) {
//		Location location = null;
//        try {        	
//            LocationManager locationManager = (LocationManager) c
//                    .getSystemService(Context.LOCATION_SERVICE);
// 
//            // getting GPS status
//            boolean isGPSEnabled = locationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
// 
//            // getting network status
//            boolean isNetworkEnabled = locationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
// 
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // no network provider is enabled
//            } else {
//               
//                // First get location from Network Provider
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            0,
//                            0, new LocationListener() {
//								
//								@Override
//								public void onStatusChanged(String provider, int status, Bundle extras) {									
//								}
//								
//								@Override
//								public void onProviderEnabled(String provider) {									
//								}
//								
//								@Override
//								public void onProviderDisabled(String provider) {									
//								}
//								
//								@Override
//								public void onLocationChanged(Location location) {									
//								}
//							});
//                    
//                    if (locationManager != null) {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);                        
//                    }
//                }
//                // if GPS Enabled get lat/long using GPS Services
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                0,
//                                0, new LocationListener() {
//									
//									@Override
//									public void onStatusChanged(String provider, int status, Bundle extras) {									
//									}
//									
//									@Override
//									public void onProviderEnabled(String provider) {										
//									}
//									
//									@Override
//									public void onProviderDisabled(String provider) {										
//									}
//									
//									@Override
//									public void onLocationChanged(Location location) {
//									}
//								});
//                        
//                        if (locationManager != null) {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);                           
//                        }
//                    }
//                }
//            }
// 
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
// 
//        return location;
		
		 try {
			 
		        // Getting GPS status
		        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		 
		        // If GPS enabled, get latitude/longitude using GPS Services
		        if (isGPSEnabled) {
		            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, this);
		            if (mLocationManager != null) {
		                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		                if (mLocation != null) {
		                    mLatitude = mLocation.getLatitude();
		                    mLongitude = mLocation.getLongitude();
		                    isLocationAvailable = true; // setting a flag that location is available
		                    return mLocation;
		                }
		            }
		        }
		 
		        // If we are reaching this part, it means GPS was not able to fetch any location
		        // Getting network status
		        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		 
		        if (isNetworkEnabled) {
		            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, this);
		            if (mLocationManager != null) {
		                mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		                if (mLocation != null) {
		                    mLatitude = mLocation.getLatitude();
		                    mLongitude = mLocation.getLongitude();
		                    isLocationAvailable = true; // setting a flag that location is available
		                                        return mLocation;
		                    }
		                }
		            }
		            // If reaching here means, we were not able to get location neither from GPS not Network,
		            if (!isGPSEnabled) {
		            // so asking user to open GPS
		                askUserToOpenGPS();
		            }
		 
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        // if reaching here means, location was not available, so setting the flag as false
		        isLocationAvailable = false;
		        return null;
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

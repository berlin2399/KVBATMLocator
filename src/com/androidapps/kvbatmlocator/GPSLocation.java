package com.androidapps.kvbatmlocator;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSLocation extends Service implements LocationListener {
	
	

	private final Context mContext;
	
	// to check whether the GPS is enabled
	boolean isGPSEnabled = false;
	
	//to check whether the network connection is enabled
	boolean isNetworkEnabled = false;
	
	boolean canGetLocation = false;
	
	Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
	
	public GPSLocation(Context mContext) {
		this.mContext = mContext;
		getLocation();
	}
	
	private Location getLocation() {
		locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
		
		isGPSEnabled  = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if(!isGPSEnabled && !isNetworkEnabled){
			
		}
		else{
			if(isNetworkEnabled){
				locationManager.requestLocationUpdates
				(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if(locationManager != null){
					 location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					 if(location != null){
						 latitude = location.getLatitude();
						 longitude = location.getLongitude();
						 canGetLocation = true;
						 
					 }
				}
			}
			
			if(isGPSEnabled){
				locationManager.requestLocationUpdates
				(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if(locationManager != null){
					 location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					 if(location != null){
						 latitude = location.getLatitude();
						 longitude = location.getLongitude();
						 canGetLocation = true;
						 
					 }
				}
			}
		}
		
		return location;
		
	}
	
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSLocation.this);
		}
	}

	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		
		alertDialog.setTitle("GPS Location Settings");
		
		alertDialog.setMessage("GPS is not enabled.Do you want to change in the Settings?");
		
		alertDialog.setPositiveButton("Settings", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});
		
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		
		alertDialog.show();
	}
	
	public double getLatitude(){
		if(location != null)
			latitude = location.getLatitude();
		
		return latitude; 
	}
	
	public double getLongitude(){
		if(location != null)
			longitude = location.getLongitude();
		
		return longitude; 
		
	}
	
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

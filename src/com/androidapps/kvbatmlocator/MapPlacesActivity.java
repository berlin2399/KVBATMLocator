package com.androidapps.kvbatmlocator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;



public class MapPlacesActivity extends Activity {

	PlaceList nearAtms;
	GoogleMap map;
	
	Marker atmMarker;
	
	double latitude;
	double longitude;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_places);
		
		Intent intent = getIntent();
		
		//get current location
		double user_latitude = Double.parseDouble(intent.getStringExtra(MainActivity.USER_LAT));
		double user_longitude = Double.parseDouble(intent.getStringExtra(MainActivity.USER_LONG));
		
		nearAtms = (PlaceList) intent.getSerializableExtra(MainActivity.NEAR_ATMS);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		Marker user_location = map.addMarker(new MarkerOptions()
		.position(new LatLng(user_latitude, user_longitude))
		.title("My location")
		.snippet("")
		.icon(BitmapDescriptorFactory.
				fromResource(R.drawable.blue_marker)));
		
		if(nearAtms.results != null){
			
			for(Place place : nearAtms.results){
				latitude = place.geometry.location.lat;
				longitude = place.geometry.location.lng;
				
				atmMarker = map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude))
				.title(place.name)
				.snippet(place.vicinity)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.red_marker)));
			}
		}
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(user_latitude, user_longitude), 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_places, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

package com.androidapps.kvbatmlocator;

import java.util.ArrayList;
import java.util.HashMap;

import com.androidapps.kvbatmlocator.util.AlertDialogManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	
	public static final String USER_LAT = "com.androidapps.kvbatmlocator.USER_LAT";
	public static final String USER_LONG = "com.androidapps.kvbatmlocator.USER_LONG";
	public static final String NEAR_ATMS = "com.androidapps.kvbatmlocator.NEAR_ATMS";
	
	boolean isInternetPresent = false;
	
	ConnectionDetector connDetector;
	
	AlertDialogManager alertDialog = new AlertDialogManager();
	
	GooglePlaces googlePlaces;
	
	PlaceList nearAtms;
	
	GPSLocation gps;
	
	Button showMapBtn;
	
	ProgressDialog progDialog;
	
	ListView lv;
	
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		connDetector = new ConnectionDetector(getApplicationContext());
		
		isInternetPresent = connDetector.isConnectedToInternet();
		if(!isInternetPresent){
			alertDialog.showAlertDialog(MainActivity.this, "Internet Connection Error"
					,"Please connect to Internet", false);
			return;
		}
		
		gps = new GPSLocation(getApplicationContext());
		
		if(gps.canGetLocation){
			Log.d("Your Location", 
					"latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
		}
		else{
			alertDialog.showAlertDialog(MainActivity.this, "GPS Error"
					,"Couldn't get location information. Please connect to GPS", false);
			return;
		}
		
		lv = (ListView) findViewById(R.id.atm_list);
		
		showMapBtn = (Button) findViewById(R.id.show_on_map_btn);
		
		new LoadPlaces().execute();
		
		showMapBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MapPlacesActivity.class);
				intent.putExtra(USER_LAT, Double.toString(gps.getLatitude()));
				intent.putExtra(USER_LONG, Double.toString(gps.getLongitude()));
				
				intent.putExtra(NEAR_ATMS, nearAtms);
				startActivity(intent);
				
			}
		});
		
	}

	class LoadPlaces extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progDialog = new ProgressDialog(MainActivity.this);
			progDialog.setMessage(Html.fromHtml("<b>Search</b><br />Loading places..."));
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(false);
			progDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			googlePlaces = new GooglePlaces();
			
			String types = "atm";
			double radius = 10000;
			String name = "\"karur+vysya+bank\"";
			
			nearAtms = googlePlaces.search(gps.getLatitude(), gps.getLongitude()
					, radius, types, name);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			progDialog.dismiss();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					//get json status from Google Search
					String status = nearAtms.status;
					
					//check for all status
					if(status.equals("OK")){
					
						if(nearAtms.results != null && !nearAtms.results.isEmpty()){
							
							HashMap<String, String> map;
							for(Place p : nearAtms.results){
								map = new HashMap<String, String>();
								
								map.put(KEY_REFERENCE, p.reference);
								map.put(KEY_NAME, p.name + ", " + p.vicinity);
								
								placesListItems.add(map);
							}
							
							ListAdapter adapter = new SimpleAdapter(MainActivity.this, placesListItems,
                                    R.layout.list_item,
                                    new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                                            R.id.reference, R.id.name });
 
                            // Adding data into listview
                            lv.setAdapter(adapter);
						}
					}
					else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                        alertDialog.showAlertDialog(MainActivity.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                    	alertDialog.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                    	alertDialog.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                    	alertDialog.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                    	alertDialog.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    }
                    else
                    {
                    	alertDialog.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
					
				}
			});
		}
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

package com.androidapps.kvbatmlocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.annotate.JacksonAnnotation;

import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

public class GooglePlaces {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	
	//Google API Key
	private static final String API_KEY = "AIzaSyCRckGF9NkFFfwombe3yWlJrQgjt9znUkQ";
	
	//Google places URLs
	private static final String PLACE_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	
	private double _latitude;
	private double _longitude;
	private double _radius;
	
	public PlaceList search(double latitude, double longitude, double radius, 
			String types, String name){
		
		this._latitude = latitude;
		this._longitude = longitude;
		this._radius = radius;
		try {	
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
		
			HttpRequest httpRequest = httpRequestFactory.buildGetRequest
					(new GenericUrl(PLACE_SEARCH_URL));
			httpRequest.getUrl().put("key", API_KEY);
			httpRequest.getUrl().put("location", _latitude + "," + _longitude);
			httpRequest.getUrl().put("radius", _radius);
			httpRequest.getUrl().put("sensor", false);
			if(types != null)
				httpRequest.getUrl().put("types", types);
			httpRequest.getUrl().put("name", name);
			
			PlaceList placeList = httpRequest.execute().parseAs(PlaceList.class);
			Log.d("Places Status", "" + placeList.status);
			return placeList;
		} catch (IOException e) {
			Log.e("Error" , e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	public PlaceDetails getPlaceDetails(String reference) throws Exception {
		try {

			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest httpRequest = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACE_DETAILS_URL));
			httpRequest.getUrl().put("key", API_KEY);
			httpRequest.getUrl().put("reference", reference);
			httpRequest.getUrl().put("sensor", "false");

			PlaceDetails place = httpRequest.execute().parseAs(PlaceDetails.class);

			return place;

		} catch (HttpResponseException e) {
			Log.e("Error in Perform Details", e.getMessage());
			throw e;
		}
	}

	
	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {
		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				HttpHeaders headers = new HttpHeaders();
				headers.setUserAgent("KVB-ATM-Locator");
				request.setHeaders(headers);
				JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
				request.setParser(parser);
			}
		});
	}
}

package com.POI.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Loader_asyc extends AsyncTask<String, Void, JSONObject> {

	ProgressDialog MyDialog;
	private Context mContext;
	InputStream is = null;
	JSONObject jObj = null;
	String json = "";
	
	JSONArray coll;
	ListView lv;
	public String[] ar;
	public static TextView tt;
	private ArrayList<HashMap<String, String>> collegeList;
	private CharSequence test;
	public static String selectedFromList;
	public static String sr1;
	public Constants constants;
	private static final String location = "location";

	HashMap<String, String> Movie_location;
	private static final String TAG_Movies = "results";
	private static final String GEO_LIST = "geometry";
	private static final String NAME= "name";
	private double lat_convert;
	private double lan_convert;
	
	String LAT_LNG;
	Marker marker;
	
	
	
	
	public Loader_asyc(Context context) {
		this.mContext = context;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		System.out.println("onPreExecute>>>>>>>>>>>>>>>>>>>>");
		MyDialog = ProgressDialog.show(mContext, " ",
				" Loading. Please wait ... ", true);
		MyDialog.show();

	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
	
		JSONParser parser = new JSONParser();
		return parser.getJSONFromUrl(params[0]);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		MyDialog.dismiss();
		Toast.makeText(mContext, "onPostExecute", Toast.LENGTH_LONG).show();

	}

	public void jsonfetch(Double lati, double langi) {
		System.out.println("MovieList.............." + collegeList);
		JSONParser jParser = new JSONParser();// Creating JSON Parser instance
		System.out.println("jParser.............." + jParser);			
		final JSONObject json = jParser.getJSONFromUrl(Constants.moviesurl(lati, langi));// getting JSON string from URL
		System.out.println("JSONObject.............." + json);						


		try {
			coll = json.getJSONArray(TAG_Movies);// Getting Array of Colleges
			
			// looping through All Contacts
			for (int i = 0; i < coll.length(); i++) {
				JSONObject json_col_code = coll.getJSONObject(i);
				System.out.println("all MovieList.............." + json_col_code);
				System.out.println("all MovieList>>>>>>>>>>" + json_col_code.length());
				// Storing each json item in variable
			
				JSONObject json_mov_geometry = json_col_code.getJSONObject(GEO_LIST);
				JSONObject json_mov_location = json_mov_geometry.getJSONObject("location");
				String lat = json_mov_location.getString("lat");
				String lan = json_mov_location.getString("lng");
					System.out.println("lat : " +  lat + "lan : " +lan);
				String loc = lat + "?" + lan;

				Movie_location = new HashMap<String, String>();
				// adding each child node to HashMap key => value					   
				Movie_location.put(GEO_LIST, loc);

				System.out.println("map" +  Movie_location);
				collegeList.add(Movie_location);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void UpdateUI(GoogleMap map){
		for (HashMap<String, String> val : collegeList){
			LAT_LNG = val.get(GEO_LIST);
			System.out.println("latslangs>>>>>>>>>>" + LAT_LNG);
			

			String latlangs[] = LAT_LNG.split("\\?");
			
			System.out.println("latsdaassssss>>>>>>>>>>" + latlangs);
			String movie_lat = latlangs[0];
			String movie_lng = latlangs[1];
			
			System.out.println("movie_lat" + movie_lat);
			System.out.println("movie_lng" + movie_lng);
			
			
			if (movie_lat != null && movie_lng !=null) {
			
					lat_convert = Double.parseDouble(movie_lat);
					lan_convert = Double.parseDouble(movie_lng);
					

				
			 marker = map
					.addMarker(new MarkerOptions()
							.position(new LatLng(lat_convert, lan_convert))
							.title("Movie Theater Name")
							.snippet("movie")
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
							.draggable(true));
				
				
		}
		System.out.println("collegeList" +  collegeList);
	}					
	
	}
	
	
	
	
	
	
	
	
public void getNearMovieTheater(final String results, final Double lat,final Double lang, final GoogleMap map ){
	
	
	Constants.moviesurl(lat, lang);
	System.out.println("current lat lang" + Constants.moviesurl(lat, lang));
	
	collegeList = new ArrayList<HashMap<String, String>>();
	Thread thread = new Thread()
	{

		@Override
	    public void run() {
	    	
	    	try{

			System.out.println("MovieList.............." + collegeList);
			JSONParser jParser = new JSONParser();// Creating JSON Parser instance
			System.out.println("jParser.............." + jParser);			
			final JSONObject json = jParser.getJSONFromUrl(Constants.moviesurl(lat, lang));// getting JSON string from URL
			System.out.println("JSONObject.............." + json);						
	
	
			coll = json.getJSONArray(results);// Getting Array of Colleges
			
			// looping through All Contacts
			for (int i = 0; i < coll.length(); i++) {
				JSONObject json_col_code = coll.getJSONObject(i);
				System.out.println("all MovieList.............." + json_col_code);
				System.out.println("all MovieList>>>>>>>>>>" + json_col_code.length());
				// Storing each json item in variable
			
				JSONObject json_mov_geometry = json_col_code.getJSONObject(GEO_LIST);
				JSONObject json_mov_location = json_mov_geometry.getJSONObject("location");
				String lat = json_mov_location.getString("lat");
				String lan = json_mov_location.getString("lng");
					System.out.println("lat : " +  lat + "lan : " +lan);
				String loc = lat + "?" + lan;

				Movie_location = new HashMap<String, String>();
				// adding each child node to HashMap key => value					   
				Movie_location.put(GEO_LIST, loc);

				System.out.println("map" +  Movie_location);
				collegeList.add(Movie_location);
			}
	
					for (HashMap<String, String> val : collegeList){
						LAT_LNG = val.get(GEO_LIST);
						System.out.println("latslangs>>>>>>>>>>" + LAT_LNG);
						

						String latlangs[] = LAT_LNG.split("\\?");
						
						System.out.println("latsdaassssss>>>>>>>>>>" + latlangs);
						String movie_lat = latlangs[0];
						String movie_lng = latlangs[1];
						
						System.out.println("movie_lat" + movie_lat);
						System.out.println("movie_lng" + movie_lng);
						
						
						if (movie_lat != null && movie_lng !=null) {
						
								lat_convert = Double.parseDouble(movie_lat);
								lan_convert = Double.parseDouble(movie_lng);
								
		
							
						 marker = map
								.addMarker(new MarkerOptions()
										.position(new LatLng(lat_convert, lan_convert))
										.title("Movie Theater Name")
										.snippet("movie")
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
										.draggable(true));
							
							
					}
					System.out.println("collegeList" +  collegeList);
				}					
				
			
			

	    	  }
	    	catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
									
	    }
	};
	thread.start();	

}

	
}

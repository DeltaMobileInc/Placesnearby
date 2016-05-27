package com.googleapi.fetcher;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.POI.utils.Constants;
import android.util.Log;

public class ApiParser {
	static JSONArray Results;
	static HashMap<String, String> Data_location;
	
	public static ArrayList<HashMap<String, String>> GoogleApiParsing(JSONObject response){
		String NAME= "name";
    	String icon_image= "icon";
    	String Address= "vicinity";
    	String photo_reference ="photo_reference";
		Log.i("ApiParser Class", response.toString());
		ArrayList<HashMap<String, String>> mData = null;
		try {
			Log.i("GOOGLE API STATUS",response.getString("status"));
			mData = new ArrayList<HashMap<String, String>>();
			Results = response.getJSONArray("results");// Getting Array of Results
			for (int i = 0; i < Results.length(); i++) {
				JSONObject json_result = Results.getJSONObject(i);
				System.out.println("all Data list.............."+ json_result);
				System.out.println("all Data list>>>>>>>>>>"+ json_result.length());
				// Storing each json item in variable
				JSONObject json_mov_geometry = json_result.getJSONObject(Constants.geo_points);
				JSONObject json_mov_location = json_mov_geometry.getJSONObject("location");
				String lat = json_mov_location.getString("lat");
				String lan = json_mov_location.getString("lng");
				System.out.println("lat : " + lat + "lan : " + lan);
				String loc = lat + "?" + lan;
				String Data_names = json_result.getString("name");
				String icon = json_result.getString("icon");
				String address = json_result.getString("vicinity");

				Data_location = new HashMap<String, String>();
				// adding each child node to HashMap key => value
				Data_location.put(Constants.geo_points, loc);
				Data_location.put(NAME, Data_names);
				Data_location.put(Address, address);
				Data_location.put(icon_image, icon);
				//
				if (json_result.has("photos")) {
					JSONArray imgs = json_result.getJSONArray("photos");
					System.out.println("photos array present");
					JSONObject Images = imgs.optJSONObject(0);
					String marker_image = Images.getString("photo_reference").trim();
					String image_width = Images.getString("width");
					System.out.println("Images with url.."+ Constants.Image(image_width, marker_image));
					Data_location.put(photo_reference, marker_image);
				}
				System.out.println("map" + Data_location);
				mData.add(Data_location);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mData;
		
	}
	
	public static ArrayList<HashMap<String, String>> GoogleApiParsingForList(JSONObject response){
			
		String NAME= "name";
    	String icon_image= "icon";
    	String Address= "vicinity";
    	String photo_reference ="photo_reference";
    	String rating = "rating";
    	ArrayList<HashMap<String, String>> mData = null;
		Log.i("ApiParser Class", response.toString());
		try {
			Log.i("GOOGLE API STATUS",response.getString("status"));
			mData = new ArrayList<HashMap<String, String>>();
			Results = response.getJSONArray("results");// Getting Array of Results
			for (int i = 0; i < Results.length(); i++) {
				JSONObject json_result = Results.getJSONObject(i);
				System.out.println("all Data list.............."+ json_result);
				System.out.println("all Data list>>>>>>>>>>"+ json_result.length());
				// Storing each json item in variable
				JSONObject json_mov_geometry = json_result.getJSONObject(Constants.geo_points);
				JSONObject json_mov_location = json_mov_geometry.getJSONObject("location");
				String lat = json_mov_location.getString("lat");
				String lan = json_mov_location.getString("lng");
				System.out.println("lat : " + lat + "lan : " + lan);
				String loc = lat + "?" + lan;
				String Data_names = json_result.getString("name");
				String icon = json_result.getString("icon");
				String address = json_result.getString("vicinity");
				

				Data_location = new HashMap<String, String>();
				// adding each child node to HashMap key => value
				Data_location.put(Constants.geo_points, loc);
				Data_location.put(NAME, Data_names);
				Data_location.put(Address, address);
				Data_location.put(icon_image, icon);
				if(json_result.has("rating")){
					String dataRating =  json_result.getString("rating");
					System.out.println("rating>>>>>"+ json_result.getString("rating"));
					Data_location.put(rating, dataRating);
				}
				if (json_result.has("photos")) {
					JSONArray imgs = json_result.getJSONArray("photos");
					System.out.println("photos array present");
					JSONObject Images = imgs.optJSONObject(0);
					String marker_image = Images.getString("photo_reference").trim();
					String image_width = Images.getString("width");
					System.out.println("Images with url.."+ Constants.Image(image_width, marker_image));
					Data_location.put(photo_reference, Constants.Image(image_width, marker_image));
				}
				System.out.println("map" + Data_location);
				mData.add(Data_location);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mData;
		
	}

}

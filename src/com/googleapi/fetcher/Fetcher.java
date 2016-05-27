package com.googleapi.fetcher;


import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.POI.utils.Constants;
import com.POI.utils.JSONParser;
import com.google.android.gms.maps.GoogleMap;

public class Fetcher  {
	
	private static ArrayList<HashMap<String, String>> mData;
	static JSONArray Results;
	static HashMap<String, String> Data_location;
//	String LAT_LNG;
//	private double lat_convert;
//	private double lan_convert;
//	Marker marker;
//	String Data_name;
//	String Data_icon;
//	String Data_Address;
   
    public static ArrayList<HashMap<String, String>> getResponseFromGoogle(final Context context,final String results, final Double lati,final Double lang, final GoogleMap map,final String apitype){
    	String NAME= "name";
    	String icon_image= "icon";
    	String Address= "vicinity";
    	String photo_reference ="photo_reference";
    
    	Constants.url(lati, lang,10000,apitype);
		System.out.println("current lat lang" + Constants.url(lati, lang,1000,apitype));
		mData = new ArrayList<HashMap<String, String>>();	
		try {
			System.out.println("DataList.............." + mData);
			JSONParser jParser = new JSONParser();// Creating JSON Parser instance
			System.out.println("jParser.............." + jParser);
			final JSONObject json = jParser.getJSONFromUrl(Constants.url(lati,lang, 10000, apitype));// getting JSON string from URL
			System.out.println("JSONObject.............." + json);
			
			if(json.length()==0){
				
			}
			
			if (json.getString("status").contains("ZERO_RESULT")) {
				Log.e("Json Response ",json.getString("status"));
			}
			else {
				Results = json.getJSONArray(results);// Getting Array of Resultseges
				// looping through All Contacts
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
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
			return mData;	
    }
	
	
//@SuppressWarnings("unchecked")
//public void getNearbyData(final Activity activity,final String results, final Double lati,final Double lang, final GoogleMap map,final String apitype ){
//		
//		Constants.url(lati, lang,10000,apitype);
//		System.out.println("current lat lang" + Constants.url(lati, lang,10000,apitype));
//		
//		progress = new ProgressDialog(activity);
//		progress.setTitle("Loading Data...");
//		progress.setMessage("please Wait!!");
//		progress.setCancelable(false);
//		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progress.show();
//		Thread thread = new Thread()
//		{
//			@Override
//		    public void run() {		    	
//		    	try{
//				System.out.println("DataList.............." + mData);
//				JSONParser jParser = new JSONParser();// Creating JSON Parser instance
//				System.out.println("jParser.............." + jParser);			
//				final JSONObject json = jParser.getJSONFromUrl(Constants.url(lati, lang, 10000,apitype));// getting JSON string from URL
//				System.out.println("JSONObject.............." + json);						
//				if (json == null){
//					
//					activity.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							System.out.println("Datas.............." + json);
//							progress = new ProgressDialog(activity);
//							progress.setTitle("");
//							progress.setMessage("No Datas found..");
//							progress.setCancelable(true);
//							progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//							progress.show();			
//						}
//					});
//				}
//				
//				else{
//				Results = json.getJSONArray(results);// Getting Array of Resultseges
//				// looping through All Contacts
//				for (int i = 0; i < Results.length(); i++) {
//					JSONObject json_result = Results.getJSONObject(i);
//					System.out.println("all Data list.............." + json_result);
//					System.out.println("all Data list>>>>>>>>>>" + json_result.length());
//					// Storing each json item in variable
//				
//					JSONObject json_mov_geometry = json_result.getJSONObject(Constants.geo_points);
//					JSONObject json_mov_location = json_mov_geometry.getJSONObject("location");
//					String lat = json_mov_location.getString("lat");
//					String lan = json_mov_location.getString("lng");
//				    System.out.println("lat : " +  lat + "lan : " +lan);
//					String loc = lat + "?" + lan;
//					String Data_names = json_result.getString("name");
//					String icon = json_result.getString("icon");					
//					String address = json_result.getString("vicinity");
//					
//					Data_location = new HashMap<String, String>();
//					// adding each child node to HashMap key => value					   
//					Data_location.put(Constants.geo_points, loc);
//					Data_location.put(NAME, Data_names);
//					Data_location.put(Address, address);
//					Data_location.put(icon_image, icon);
//					//
//					if(json_result.has("photos")){
//						JSONArray imgs =  json_result.getJSONArray("photos");
//						 System.out.println("photos array present");
//						 JSONObject Images = imgs.optJSONObject(0);
//						 String marker_image = Images.getString("photo_reference").trim();
//						 String image_width = Images.getString("width");
//								 
//						 //Constants.Image(marker_image,image_width);
//						 System.out.println("Images with url.."+ Constants.Image(image_width,marker_image));
//						 
//						 Data_location.put(photo_reference, marker_image);
//					}
//					System.out.println("map" +  Data_location);
//					mData.add(Data_location);
//				}
//			
//				activity.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						for (final HashMap<String, String> val :mData ){
//							LAT_LNG = val.get(Constants.geo_points);
//							Data_name = val.get(NAME);
//							Data_icon = val.get(icon_image);
//							Data_Address = val.get(Address);						
//							Log.e("Image", Data_icon);
//							System.out.println("Data_icone>>>>>>>>>>"+ Data_icon);
//							System.out.println("Data name>>>>>>>>>>"+ Data_name);
//							System.out.println("latslangs>>>>>>>>>>" + LAT_LNG);							
//							String latlangs[] = LAT_LNG.split("\\?");
//							
//							System.out.println("latsdaassssss>>>>>>>>>>" + latlangs);
//							String Data_lat = latlangs[0];
//							String Data_lng = latlangs[1];
//							
//							System.out.println("Data latitude" + Data_lat);
//							System.out.println("Data Longitude" + Data_lng);							
//							
//							if (Data_lat != null && Data_lng !=null) {
//							
//									lat_convert = Double.parseDouble(Data_lat);
//									lan_convert = Double.parseDouble(Data_lng);
//	
//							 marker = map
//									.addMarker(new MarkerOptions()
//											.position(new LatLng(lat_convert, lan_convert))
//											.title(Data_name)
//											.snippet(Data_Address)
//											.icon(BitmapDescriptorFactory
//													.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
//											.draggable(true));	
//							 												
//						}
//						System.out.println("Data_list" +  mData);	
//					}	
//						 map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
//								
//								@SuppressWarnings("deprecation")
//								@Override
//								public void onInfoWindowClick(Marker arg0) {
//									// TODO Auto-generated method stub
//									AlertDialog alertDialog = new AlertDialog.Builder(
//											activity).create();					 		
//							        // Setting Dialog Title
//							        alertDialog.setTitle(arg0.getTitle());
//							        // Setting Dialog Message
//							        alertDialog.setMessage(arg0.getSnippet());
//							 
//							        // Setting Icon to Dialog
//							        alertDialog.setIcon(R.drawable.ic_launcher);
//							 
//							        // Setting OK Button
//							        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//							                public void onClick(DialogInterface dialog, int which) {
//							                // Write your code here to execute after dialog closed
//							                //Toast.makeText(activity, "You clicked on OK", Toast.LENGTH_SHORT).show();
//							                }
//							        });
//					 
//					        // Showing Alert Message
//					        alertDialog.show();										
//								}
//							});
//						
////						progress.dismiss();
//					}
//				});	
//				
//				}
//
//		    	  }
//		    	catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//										
//		    }
//			
//		};
//		thread.start();	
//
//	}

}

package com.POI.utils;

import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public abstract class Constants extends Activity {


	public static final String geo_points = "geometry";
	static ProgressDialog progress;
	public static String moviesurl(Double Latitude ,Double Longitude){
		
		final String movie_url = "https://maps.googleapis.com/maps/api/place/search/json?" +
				"location=" + Latitude +"," + Longitude +"&radius=10000&" +
				"types=movie_theater&sensor=false&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
		return movie_url;
		
	}
	
	
public static String Atmurl(Double Latitude ,Double Longitude){
		
		final String atm_url = "https://maps.googleapis.com/maps/api/place/search/json?" +
				"location=" + Latitude +"," + Longitude +"&radius=10000&" +
				"types=atm&sensor=false&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
		return atm_url;
		
	}
	
public static String Food(Double Latitude ,Double Longitude){
	
	final String food_url = "https://maps.googleapis.com/maps/api/place/search/json?" +
			"location=" + Latitude +"," + Longitude +"&radius=10000&" +
			"types=food&sensor=false&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
	return food_url;
	
}
	
public static String Restrunt(Double Latitude ,Double Longitude){
	
	final String food_url = "https://maps.googleapis.com/maps/api/place/search/json?" +
			"location=" + Latitude +"," + Longitude +"&radius=10000&" +
			"types=restaurant&sensor=false&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
	return food_url;
	
}

public static String Bar(Double Latitude ,Double Longitude){
	
	final String food_url = "https://maps.googleapis.com/maps/api/place/search/json?" +
			"location=" + Latitude +"," + Longitude +"&radius=10000&" +
			"types=bar&sensor=false&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
	return food_url;

}


public static String url(Double Latitude ,Double Longitude, int radius , String apitype ){
	
	final String food_url = "https://maps.googleapis.com/maps/api/place/search/json?" +
			"location=" + Latitude +"," + Longitude +"&radius=" +radius +  "&" +
			"types=" + apitype + "&sensor=false&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
	return food_url;
	
}

public static String Image(String width,String image){
	
	final String image_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+width+
	"&photoreference="+image+"&key=AIzaSyAXTnzY6fc6wG_ilxDHkeYqpndnEFXaKz8";
	return image_url;

}

public static void showMessage(Context context, String title, String message){
	AlertDialog alert = new AlertDialog.Builder(context).create();
    alert.setTitle(title);
    alert.setMessage(message);
    alert.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub	
		}
	});

    alert.show();
}

public static boolean haveNetworkConnection(Context context) {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    for (NetworkInfo ni : netInfo) {
        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
            if (ni.isConnected())
                haveConnectedWifi = true;
        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
            if (ni.isConnected())
                haveConnectedMobile = true;
    }
    return haveConnectedWifi || haveConnectedMobile;
}

public static void clearMapBeforeLoadAndAnimate(GoogleMap map,Double latitude, Double longitude ){
	map.clear();
	map.setMyLocationEnabled(true);
	LatLng latlang = new LatLng(latitude, longitude);
	map.moveCamera(CameraUpdateFactory.newLatLng(latlang));
	map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,(float) 9)); 
}

public static void showProgress(Context context, String text, String title){
	progress = new ProgressDialog(context);
	progress.setTitle(title);
	progress.setMessage(text);
	progress.setCancelable(true);
	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progress.show();
}

public static void stopProgres(){
	progress.dismiss();
	
}

public static HashMap<String, String> Configuration(){
	
	HashMap<String, String> apiConfig = new HashMap<String, String>();
	apiConfig.put("MovieTheater", "movie_theater");
	apiConfig.put("Atm", "movie_theater");
	apiConfig.put("Food", "food");
	apiConfig.put("Restaurant", "restaurant");
	apiConfig.put("Bar", "bar");
	apiConfig.put("Bank", "bank");
	apiConfig.put("Cafe", "cafe");
	apiConfig.put("Railway", "train_station");
	apiConfig.put("University", "university");
	apiConfig.put("Spa", "spa");
	apiConfig.put("Night club", "night_club");
	apiConfig.put("Rent a car", "car_rental");
	apiConfig.put("Bakery", "bakery");
	apiConfig.put("Gas Station", "gas_station");
	
	return apiConfig;
	
}

}

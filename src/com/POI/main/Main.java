package com.POI.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.POI.utils.Constants;
import com.POI.utils.NetworkCall;
import com.POI.utils.RequestCompletion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googleapi.fetcher.ApiParser;
import com.POI.main.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends FragmentActivity implements LocationListener,LocationSource, RequestCompletion {
	private String TAG = this.getClass().getSimpleName();
	private DrawerLayout mDrawerLayout;
	ListView viewActionsList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence title;
	List<RowItem> rowItems;
	public Constants constants;
	Menu menu;
	Thread t;
	Fragment fragment = null;
	private double lat_convert;
	private double lan_convert;
	ArrayList<HashMap<String, String>> dataArrayListHasmap;
	private static long mBackpressed;
	final static String[] values = new String[] { "MovieTheater", "Atm", "Food",
										"Restaurant", "Bar", "Bank", 
										"Cafe", "Railway", "University", 
										"Spa" ,"Night club","Rent a car",
										"Bakery", "Gas Station"};
	public int position;
	public static final Integer[] images = {R.drawable.movie_theater,
											R.drawable.atm,
											R.drawable.food, 
											R.drawable.restarunt,
											R.drawable.bar, 
											R.drawable.bank, 
											R.drawable.cafe,
											R.drawable.railway, 
											R.drawable.university,
											R.drawable.spa,
											R.drawable.nightclub, 
											R.drawable.rentcar, 
											R.drawable.bakery ,
											R.drawable.fuel};
	private GoogleMap mMap;
    Fragment Loadmapview;
	JSONArray Results;
	HashMap<String, String> Data_location;
	LocationManager lmanager;
	private boolean isGPSEnabled, isNetworkEnabled;
	boolean canGetLocation = false;
	Location location; 
	double latitude;
	double longitude;
	Marker marker;
	ArrayList<HashMap<String, String>> Items;
	ListAdapter adapter;
	HashMap<String, String> map;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
	private static final long MIN_TIME_BW_UPDATES = 1000;
	ProgressDialog MyDialog;
	HashMap<String, String> Movie_location;
	String LAT_LNG;
	String Movie_Theater_Name;
	ImageView listbtn;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_draw);
		listbtn =(ImageView) findViewById(R.id.load_list);
		listbtn.setVisibility(View.INVISIBLE);

		if (savedInstanceState != null) {
			position = savedInstanceState.getInt("position");
		}
		getGoogleMap();
		leftVerticalMenu();
		listbtn.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Clkicked button");
				Constants.showProgress(Main.this, "Please wait...", "Loading...");
				System.out.println(TAG + latitude + "," +longitude + Constants.Configuration().get(getActionBar().getTitle()));
				String apiType = Constants.Configuration().get(getActionBar().getTitle());
				ListDataActivity listDatafragment = new ListDataActivity(Main.this,latitude,longitude,apiType );
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.content_frame, listDatafragment, "List");
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});	
	}
	
	public void leftVerticalMenu(){
		title = getActionBar().getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		viewActionsList = (ListView) findViewById(R.id.left_drawer);
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < values.length; i++) {
			RowItem item = new RowItem(images[i], values[i]);
			rowItems.add(item);
		}
		CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.drawer_item, rowItems);
		viewActionsList.setAdapter(adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			// ** Called when a drawer has settled in a completely closed state.
			// *//*
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(title);
				System.out.println("onDrawerClosed..............");
			}
			// ** Called when a drawer has settled in a completely open state.
			// *//*
			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle("Open Drawer");
				System.out.println("onDrawerOpened..............");
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		viewActionsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				mDrawerLayout.closeDrawer(viewActionsList);
				mDrawerLayout
						.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

							@Override
							public void onDrawerClosed(View drawerView) {
								super.onDrawerClosed(drawerView);
								//System.out.println("onDrawerClosed.....called");
								mDrawerLayout.closeDrawer(viewActionsList);
							}
							@Override
							public void onDrawerSlide(View drawerView,
									float slideOffset) {
								super.onDrawerSlide(drawerView, slideOffset);
								//System.out.println("onDrawerSlide.....called");
							}
							@Override
							public void onDrawerStateChanged(int newState) {
								// TODO Auto-generated method stub
								//System.out.println("onDrawerStateChanged.....called");
							}
							@Override
							public void onDrawerOpened(View drawerView) {
								// TODO Auto-generated method stub
								super.onDrawerOpened(drawerView);
								//System.out.println("onDrawerOpened.....called");
							}
						});
				mDrawerLayout.closeDrawer(viewActionsList);
				showFragment(position);
			}
		});
		
		
	}
	
	public GoogleMap getGoogleMap() {
		if (mMap == null && this != null && this.getSupportFragmentManager() != null) {
			SupportMapFragment smf = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
			if (smf != null) {
				mMap = smf.getMap();
				mMap.setBuildingsEnabled(true);
				getcurrentLocation();
				Constants.clearMapBeforeLoadAndAnimate(mMap, latitude, longitude);
			}
		}
	 return mMap;
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
				mDrawerLayout.openDrawer(viewActionsList);
			}
		}, 2000);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);		
		menu.findItem(R.id.action_settings).setVisible(false);
		menu.findItem(R.id.action_search).setVisible(false);
		menu.findItem(R.id.rateus).setVisible(true);				
		MenuItem item = menu.findItem(R.id.rateus);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				launchMarket();				
				return true;
			}
		});		
		return super.onCreateOptionsMenu(menu);
	}
	
	private void launchMarket() {
	    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.POI.main&hl=en"+ getPackageName());
	    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
	    try {
	        startActivity(goToMarket);
	    } catch (ActivityNotFoundException e) {
	        Toast.makeText(this, "Could not launch", Toast.LENGTH_LONG).show();
	    }
	}

	private void showFragment(int position) {
		this.position = position;
		switch (position) {
		case 0:
			
			if(mMap == null){
				Log.e("Marker", "No Markers in movies.....");
			}
			else{
				getDataFromNetwork("movie_theater");
		}
			
			break;
				
		case 1:
			
			if(mMap == null){
				Log.e("Marker", "No Markers in atms....");
			}
			else{
				getDataFromNetwork("atm");

			}
			
			break;
						
          case 2:
			
			if(mMap == null){
				Log.e("Marker", "No Markers in Food....");
			}
			else{
				getDataFromNetwork("food");
			}
			
			
			break;
			
          case 3:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in restaurant....");
  			}
  			else{
  				getDataFromNetwork("restaurant");
		}
  			break;
			
  			
          case 4:
    			
    			if(mMap == null){
    				Log.e("Marker", "No Markers in bar....");
    			}
    			else{
    				getDataFromNetwork("bar");
    			}
    			break;
			
          case 5:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in bank....");
  			}
  			else{
  				getDataFromNetwork("bank");

  			}
  			
  			
  			break;
          case 6:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in cafe....");
  			}
  			else{
  				getDataFromNetwork("cafe");
			
  			}
  			
  			
  			break;
          case 7:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in train_station....");
  			}
  			else{
  				getDataFromNetwork("train_station"); 				
  			}
  			
  			
  			break;
          case 8:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in university....");
  			}
  			else{
  				getDataFromNetwork("university");
  				
  			} 			

  			break;
  			
          case 9:
    			
    			if(mMap == null){
    				Log.e("Marker", "No Markers in spa....");
    			}
    			else{
    				getDataFromNetwork("spa");
    				
    			}
    			
    			break;
          case 10:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in night_club....");
  			}
  			else{
  				getDataFromNetwork("night_club");

  				
  			}
  			
  			break;
          case 11:
    			
    			if(mMap == null){
    				Log.e("Marker", "No Markers in car_rental....");
    			}
    			else{
    				getDataFromNetwork("car_rental");   				
    			}
    			
    			break;
  			
          case 12:
  			
  			if(mMap == null){
  				Log.e("Marker", "No Markers in bakery....");
  			}
  			else{
  				getDataFromNetwork("bakery");
  				
  			}
  			
  			break;
          case 13:
    			
    			if(mMap == null){
    				Log.e("Marker", "No Markers in bakery....");
    			}
    			else{
    				getDataFromNetwork("gas_station");
		}
    			break;			
		default:
			return;
		}
		viewActionsList.setItemChecked(position, true);
		getActionBar().setTitle((values[position]));
	}
		
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    savedInstanceState.putInt("position", position);
	    // Read values from the "savedInstanceState"-object and put them in your textview
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("position", position);
		//System.out.println("onSaveInstanceState called");
	}

	@Override
	protected void onStop() {
		Log.e("onDestroy", "called on stop...........");
		super.onStop();

	}
	@Override
	protected void onResume() {
		super.onResume();
//		getActionBar().setTitle((values[position]));
		getActionBar().setTitle(title);
  
	}
	public void getcurrentLocation() {
		Constants.showProgress(this, "Lodaing Current Location...", "");
		// To navigate to the user current location
		lmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		isGPSEnabled = lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);// getting GPS																				// status
		isNetworkEnabled = lmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// getting network
		if (!isGPSEnabled && !isNetworkEnabled) {
			// No network provider is enabled
			Toast.makeText(this, "Please enable GPS settings...",Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		} else {
			this.canGetLocation = true;
			if (isNetworkEnabled) {
				mMap.setMyLocationEnabled(true);
				lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
																MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d(TAG, "Network" + isNetworkEnabled);
				if (lmanager != null) {
					location = lmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						mMap.setMyLocationEnabled(true);
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						System.out.println("latitude>>>>>>>>." + latitude +":"+longitude);
					}
				}
			} 
			else {
				Toast.makeText(this,"Notwork provider is not enabled", Toast.LENGTH_LONG).show();
			}
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
																	MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d(TAG, "GPS Enabled" + isGPSEnabled);
					if (lmanager != null) {
						location = lmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							mMap.setMyLocationEnabled(true);
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							Toast.makeText(this,latitude + "," + longitude,Toast.LENGTH_LONG).show();
						}
						else {
							Toast.makeText(this,"GPS Enabled and No Location Found",Toast.LENGTH_LONG).show();
						}
					}
				} else {
//					Toast.makeText(this,"GPS Enabled and No Location Found",Toast.LENGTH_LONG).show();
				}
			}
		}
		Constants.stopProgres();
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestCompletion(final JSONObject response) {
		Log.i("Main Activity", response.toString());
		try {
			if (response.getString("status").contains("ZERO_RESULT")) {
				Constants.showMessage(this, "Sorry", "No data near you");
				Constants.stopProgres();
			}
			else {
				ArrayList<HashMap<String, String>> dataArrayListHasmap = ApiParser.GoogleApiParsing(response);
				Log.i("dataArrayListHasmap", dataArrayListHasmap.toString());
				updateUI(dataArrayListHasmap);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	

	}
	@Override
	public void onRequestCompletionError(String error) {
		Constants.stopProgres();
		Constants.showMessage(this, "Sorry", error);
	}

	private void getDataFromNetwork(String apiType){
		mMap.clear();
		boolean connectionStatus = Constants.haveNetworkConnection(this);
    	if(connectionStatus==true){
	   		 Log.e(TAG, "Connected to Internet");
	   		 Constants.showProgress(this, "Loading Nearby "+Main.values[position]+ "s..." , Main.values[position]);
	   		 NetworkCall call = new NetworkCall(this);
	   		 call.GooglePlacesNetworCall("results", latitude, longitude, apiType);
	   	 }
	   	 else{
	   		 Log.e(TAG, "Check Internet connectivity");
	   		 Constants.showMessage(this, "Sorry", "Check Internet connectivity");
	   	 }
		
	}
	private void updateUI(final ArrayList<HashMap<String, String>> dataArrayListHasmap){
		Marker marker;
		String Data_name;
		String Data_icon;
		String Data_Address;
		String LAT_LNG;
		String NAME= "name";
		String icon_image= "icon";
		String Address= "vicinity";
		 LatLng latlang = new LatLng(latitude, longitude);
		 mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));
		 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,(float) 10));
	    	for (final HashMap<String, String> val :dataArrayListHasmap ){
				LAT_LNG = val.get(Constants.geo_points);
				Data_name = val.get(NAME);
				Data_icon = val.get(icon_image);
				Data_Address = val.get(Address);						
				Log.e("Image", Data_icon);
				System.out.println("Data_icone>>>>>>>>>>"+ Data_icon);
				System.out.println("Data name>>>>>>>>>>"+ Data_name);
				System.out.println("latslangs>>>>>>>>>>" + LAT_LNG);							
				String latlangs[] = LAT_LNG.split("\\?");
				System.out.println("latsdaassssss>>>>>>>>>>" + latlangs);
				String Data_lat = latlangs[0];
				String Data_lng = latlangs[1];
				System.out.println("Data latitude" + Data_lat);
				System.out.println("Data Longitude" + Data_lng);							
				if (Data_lat != null && Data_lng !=null) {
					lat_convert = Double.parseDouble(Data_lat);
					lan_convert = Double.parseDouble(Data_lng);
					marker = mMap.addMarker(new MarkerOptions()
					 			 .position(new LatLng(lat_convert, lan_convert))
					 			 .title(Data_name).snippet(Data_Address)
								 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
								 .draggable(true));	
				 												
				}
	    	}	
	    	mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
				@Override
				public View getInfoWindow(Marker arg0) {
					// TODO Auto-generated method stub
					return null;
				}
				@Override
				public View getInfoContents(Marker arg0) {
					// TODO Auto-generated method stub
					LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
					View view = inflater.inflate(R.layout.custom_map_snipet, null);
					TextView txt1 = (TextView)view.findViewById(R.id.title);
					TextView txt2 = (TextView)view.findViewById(R.id.address);
					txt1.setText(arg0.getTitle());
					txt2.setText(arg0.getSnippet());
					return view;
				}
			});
	    	mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onInfoWindowClick(Marker arg0) {
						AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();					 		
				        alertDialog.setTitle(arg0.getTitle());
				        alertDialog.setMessage(arg0.getSnippet());
	//			         alertDialog.setIcon(Main.images[position]);
				        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int which) {
				                // Write your code here to execute after dialog closed
				                }
				        });
		        // Showing Alert Message
		        alertDialog.show();
	//	        dialog.show();
					}
				});
	    	Constants.stopProgres();
	    	listbtn.setVisibility(View.VISIBLE);
		}

	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount() >0){
			 getFragmentManager().popBackStack();
		}
		else{
			if(mBackpressed+2000 > System.currentTimeMillis()){
				super.onBackPressed();
			}else{
				Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_LONG).show();
				mBackpressed =  System.currentTimeMillis();
			}
		}
	}

}
	

package com.POI.main;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import com.POI.CustomList.CustomAdapter;
import com.POI.utils.Constants;
import com.POI.utils.NetworkCall;
import com.POI.utils.RequestCompletion;
import com.googleapi.fetcher.ApiParser;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
public class ListDataActivity extends Fragment implements RequestCompletion {
	
	FragmentActivity fragmentActivityContext;
	double latitude; 
	double longitude;
	String apitype;
	ListView listView;
	
	public  ListDataActivity(FragmentActivity fragmentActivityContext,double latitude, double longitude,String apitype){
			this.fragmentActivityContext = fragmentActivityContext;
			this.latitude = latitude;
			this.longitude = longitude;
			this.apitype = apitype;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View customListView = inflater.inflate(R.layout.listdata, container, false);
		 listView = (ListView)customListView.findViewById(R.id.list_view);
		 makeNetworkCall(); 
		return listView;
	}
	
	private void makeNetworkCall(){
		NetworkCall call = new NetworkCall(this);
		System.out.println("ListDataActivity>>>>>"+ latitude +","+ longitude + apitype);
		call.GooglePlacesNetworCall("results", latitude, longitude, apitype);
	}

	@Override
	public void onRequestCompletion(JSONObject response) {
		Constants.stopProgres();
		UpdateUI(response);	
	}

	@Override
	public void onRequestCompletionError(String error) {
		// TODO Auto-generated method stub
		Constants.stopProgres();
		Constants.showMessage(getActivity(), error, "Sorry");
		
	}
	
	public void UpdateUI(JSONObject response){
		ArrayList<HashMap<String, String>> listArray=ApiParser.GoogleApiParsingForList(response);
		CustomAdapter listAdapter = new CustomAdapter(this, listArray);
		listView.setAdapter(listAdapter);
		
//		if (getFragmentManager().getBackStackEntryCount() == 0) {
//
//	        //Stack Zero
//	    } else {
//	        getFragmentManager().popBackStack();
//	        removeCurrentFragment();
//	        //stacknotzeo;
//	    }
	}
	
	public void removeCurrentFragment() {

	    Fragment currentFrag = (Fragment) getFragmentManager()
	            .findFragmentById(R.id.content_frame);

	    if (currentFrag != null)
	        getFragmentManager().beginTransaction().remove(currentFrag);

	    getFragmentManager().beginTransaction().commit();

	}
}

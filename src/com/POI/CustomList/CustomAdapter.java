package com.POI.CustomList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.POI.main.R;
import com.POI.utils.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class CustomAdapter extends BaseAdapter {	
	Fragment fContext ;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> listData;
	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomAdapter(Fragment fContext, ArrayList<HashMap<String, String>> listData) {
		this.fContext = fContext;
		this.listData = listData;
		
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int location) {
		return listData.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) fContext.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.customlistadapter, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView address = (TextView) convertView.findViewById(R.id.address);
		
		
		String ratingBarData = listData.get(position).get("rating");
		if(ratingBarData == null){
			RatingBar rating = (RatingBar)convertView.findViewById(R.id.ratingBar);
			rating.setRating(0);
			
		}else{
			RatingBar rating = (RatingBar)convertView.findViewById(R.id.ratingBar);
			float ratingStringToFloat=Float.parseFloat(ratingBarData);
			rating.setRating(ratingStringToFloat);
		}
		NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
		String image = listData.get(position).get("photo_reference");
		if(image == null){
			profilePic.setImageUrl(listData.get(position).get("icon"), imageLoader);
		}
		else{
			profilePic.setImageUrl(listData.get(position).get("photo_reference"), imageLoader);
		}
		name.setText(listData.get(position).get("name"));
		address.setText(listData.get(position).get("vicinity"));
		return convertView;
	}

}

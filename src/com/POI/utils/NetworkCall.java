package com.POI.utils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class NetworkCall { 
	 public Fragment fContext;
	 private RequestCompletion mRequestCompletion;
	 FragmentActivity fcontextactivity;
	
	public NetworkCall(FragmentActivity fcontextactivity ){
			this.fcontextactivity = fcontextactivity;
			mRequestCompletion=(RequestCompletion)fcontextactivity;
	}
	
	public NetworkCall(Fragment fContext){
		this.fContext = fContext;
		mRequestCompletion=(RequestCompletion)fContext;
	}

	public JsonObjectRequest GooglePlacesNetworCall(final String results, final Double lati,final Double lang, final String apitype){
		
		 final String GOOGLE_API_URL = Constants.url(lati, lang,1000,apitype);
		 System.out.println("request" +GOOGLE_API_URL);
		 
		 JsonObjectRequest req = null;
			try {
				req = new JsonObjectRequest(Request.Method.GET,GOOGLE_API_URL, null,
				        new Response.Listener<JSONObject>() {						
							@Override
				            public void onResponse(JSONObject response) {
				                // handle response
				            	 Log.d("Google Response...", response.toString());
				            	mRequestCompletion.onRequestCompletion(response);
				            	
							}
				        }
				,new Response.ErrorListener() {
		        	@Override
					public void onErrorResponse(VolleyError error) {
		        		handleNetworkError(error);															
					}			            						            	
		        }); 
				
			    Log.d("GoogleReq", req.toString());
			    req.setRetryPolicy(
			            new DefaultRetryPolicy(
			                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
			                    0,
			                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));	
//			    req.setShouldCache(true);
			 // Adding request to volley request queue
			    AppController.getInstance().addToRequestQueue(req);    
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return req;
		 
		
	}
	
	/** 
	 * @param Handling the Volley Network Errors.
	 */
	public void handleNetworkError(VolleyError error){

		// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
		// For AuthFailure, you can re login with user credentials.
		// For ClientError, 400 & 401, Errors happening on client side when sending api request.
		// In this case you can check how client is forming the api and debug accordingly.
		// For ServerError 5xx, you can do retry or handle accordingly.
		NetworkResponse response = error.networkResponse;
        String json = null;						        
        if(response != null && response.data != null){
            switch(response.statusCode){
                case 400:
                case 401:
                     json = new String(response.data);
                     Log.d("Volley error",""+json);
//                     json = trimMessage(json, "message");
//                     if(json != null) displayMessage(json);
                     break;
                }
        }
		if( error instanceof NetworkError) {
		} else if( error instanceof ClientError) {
			Log.d("ClientError", error.getMessage());
			mRequestCompletion.onRequestCompletionError(error.getMessage());
		} else if( error instanceof ServerError) {
			Log.d("ServerError", error.getMessage());
			mRequestCompletion.onRequestCompletionError(error.getMessage());
		} else if( error instanceof AuthFailureError) {
			Log.d("AuthFailureError", error.getMessage());
			mRequestCompletion.onRequestCompletionError(error.getMessage());
		} else if( error instanceof ParseError) {
			Log.d("ParseError", error.getMessage());
			mRequestCompletion.onRequestCompletionError("TimeoutError");
		} else if( error instanceof NoConnectionError) {
			Log.d("NoConnectionError", error.getMessage());
			mRequestCompletion.onRequestCompletionError("Please connect to network...");
		} else if( error instanceof TimeoutError) {
			mRequestCompletion.onRequestCompletionError("TimeoutError");
		}	
	
	}

}

package com.POI.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;

 
public class JSONParser {
 
    static InputStream is=null ;
    static JSONObject jObj = null;
    static String json = "";
    ProgressDialog MyDialog;
 
    // constructor
    public JSONParser() {
    }
    public JSONObject getJSONFromUrl(String url) {
 
        // Making HTTP request
        try {
        	HttpParams httpParameters = new BasicHttpParams();
        	HttpConnectionParams.setConnectionTimeout(httpParameters, 10000000);
        	HttpConnectionParams.setSoTimeout(httpParameters, 15000000);       	
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            System.out.println("httpClient.............." + httpClient);           
            HttpPost httpPost = new HttpPost(url);
            System.out.println("httpPost.............." + url); 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) { 
                Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
                return null;
             }            
//          System.out.println("httpResponse.............." + httpResponse);
            HttpEntity httpEntity = httpResponse.getEntity();
           //System.out.println("httpEntity.............." + httpEntity);         
               is = httpEntity.getContent();
               Log.i("JSONParser", "is ok");
        } 

        
       
        catch(ConnectTimeoutException e){
        	 e.printStackTrace();
        	 System.out.println("ConnectTimeoutException.............." + e);
        	 
        }

        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("UnsupportedEncodingException:","Http Response:"+e);
            
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.d("ClientProtocolException:","Http Response:"+e);
            
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("IOException:","Http Response:"+e);
        }
         
        try {
        	 BufferedReader reader = new BufferedReader(new InputStreamReader(
                     is, "iso-8859-1"), 8);
             StringBuilder sb = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null) {
                 sb.append(line + "\n");
             }
             is.close();
             json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        
        // return JSON String
        return jObj;
        
    }
    

}
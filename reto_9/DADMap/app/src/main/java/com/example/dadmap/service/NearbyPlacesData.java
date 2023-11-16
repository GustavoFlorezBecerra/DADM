package com.example.dadmap.service;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dadmap.BuildConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NearbyPlacesData extends AsyncTask<Object, String, String> {

    private String nearbyPlacesData = "";
    private GoogleMap mMap;


    @Override
    protected String doInBackground(Object... objects) {

        try {
            mMap = (GoogleMap) objects[0];
            String url = (String) objects[1];
            PlacesService placesService = new PlacesService();
            nearbyPlacesData = placesService.getPlacesData(url);

        } catch(IOException e) {
            Log.d("Error NearbyPlacesData", e.getMessage());
        }

        return nearbyPlacesData;
    }

    @Override
    protected void onPostExecute(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectAttr = jsonArray.getJSONObject(i);
                JSONObject location = jsonObjectAttr.getJSONObject("geometry").getJSONObject("location");

                LatLng latLng = new LatLng(Double.parseDouble(location.getString("lat")), Double.parseDouble(location.getString("lng")));

                JSONArray types = jsonObjectAttr.getJSONArray("types");

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(jsonObjectAttr.getString("name"));
                markerOptions.position(latLng);
                markerOptions.snippet(types.getString(0));

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }

            if(jsonObject.has("next_page_token")){
                getPlacesNextPages(jsonObject.getString("next_page_token"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search nearby places when the search result has aditional pages.
     *
     * @param nextPageToken
     */
    private void getPlacesNextPages(String nextPageToken) {
        StringBuilder url = new StringBuilder();
        url.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        url.append("pagetoken=");
        url.append(nextPageToken);
        url.append("&key=");
        url.append(BuildConfig.MAPS_API_KEY);

        Object[] fetchData = new Object[2];
        fetchData[0] = mMap;
        fetchData[1] = url.toString();

        NearbyPlacesData nearbyPlacesData = new NearbyPlacesData();
        nearbyPlacesData.execute(fetchData);
    }
}

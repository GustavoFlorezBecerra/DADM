package com.example.dadmap.service;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlacesService {

    public String getPlacesData(String url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String placesData = "";

        try {
            URL getUrl = new URL(url);
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();

            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();

            String line = "";

            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();

            placesData = buffer.toString();

        } catch(Exception e){
            Log.d("Exception get places", e.getMessage());
        } finally {
            inputStream.close();
            connection.disconnect();
        }

        return placesData;
    }
}

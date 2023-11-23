package com.example.dadmwebservices.service;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dadmwebservices.MainActivity;
import com.example.dadmwebservices.dto.CatalogoDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogData extends AsyncTask<Object, String, String>  {

    private String catalogData = "";

    private List<CatalogoDTO> catalogo = new ArrayList<>();

    @Override
    protected String doInBackground(Object... objects) {

        try {
            String url = (String) objects[0];
            CatalogService catalogService = new CatalogService();
            catalogData = catalogService.getCatalogData(url);

        } catch(IOException e) {
            Log.d("Error catalogData", e.getMessage());
        }

        return catalogData;
    }
}

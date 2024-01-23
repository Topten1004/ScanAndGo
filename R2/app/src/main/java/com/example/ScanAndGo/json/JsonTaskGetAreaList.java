package com.example.ScanAndGo.json;

import android.os.AsyncTask;

import com.example.ScanAndGo.dto.Area;
import com.example.ScanAndGo.dto.Building;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonTaskGetAreaList extends AsyncTask<String, String, List<Area>> {

    public JsonTaskGetAreaList() {
        super();
    }

    protected List<Area> doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("Authorization", "Bearer " + params[1]);
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) buffer.append(line+"\n");

            boolean authOk = buffer.length() < 15;
            if(!authOk) authOk = !buffer.substring(0, 15).equals("<!DOCTYPE html>");
            if(authOk){

                String jsonString = buffer.toString();

                List<Area> locations = parseJsonToList(jsonString);

                return locations;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<Area> parseJsonToList(String jsonString) {
        List<Area> locations = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                int buildingId = jsonObject.getInt("building_id");
                String name = jsonObject.getString("name");

                Area location = new Area(id, buildingId,name);
                locations.add(location);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locations;
    }

    @Override
    protected void onPostExecute(List<Area> result) {
        super.onPostExecute(result);
    }
}
package com.example.ScanAndGo.json;

import android.os.AsyncTask;

import com.example.ScanAndGo.dto.Category;
import com.example.ScanAndGo.dto.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonTaskGetItemList extends AsyncTask<String, String, List<Item>> {

    public JsonTaskGetItemList() {
        super();
    }

    protected List<Item> doInBackground(String... params) {

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

                List<Item> items = parseJsonToList(jsonString);

                return items;
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

    private List<Item> parseJsonToList(String jsonString) {
        List<Item> items = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int categoryId = jsonObject.getInt("category_id");
                String barcode = jsonObject.has("barcode") ? jsonObject.getString("barcode") : "";

                Item tempItem = new Item(id, categoryId, name, barcode);
                items.add(tempItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }
    @Override
    protected void onPostExecute(List<Item> result) {
        super.onPostExecute(result);
    }
}
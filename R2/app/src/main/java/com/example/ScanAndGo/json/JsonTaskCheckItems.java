package com.example.ScanAndGo.json;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ScanAndGo.dto.Category;
import com.example.ScanAndGo.dto.ResponseCheckItem;
import com.example.ScanAndGo.dto.ResponseCheckItems;
import com.example.ScanAndGo.dto.ResponseCheckTag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonTaskCheckItems extends AsyncTask<String, String, List<ResponseCheckItem>> {

    public JsonTaskCheckItems() {
        super();
    }

    protected List<ResponseCheckItem> doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setChunkedStreamingMode(0);
            connection.setInstanceFollowRedirects(true); // Add this line

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                writer = new BufferedWriter(new OutputStreamWriter(
                        out, StandardCharsets.UTF_8));
            }
            writer.write(params[1]);
            writer.flush();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) buffer.append(line);

            boolean authOk = buffer.length() < 15;
            if(!authOk) authOk = !buffer.substring(0, 15).equals("<!DOCTYPE html>");
            if(authOk){

                String jsonString = buffer.toString();
                Log.d("CheckItemsList::::::", jsonString);

                List<ResponseCheckItem> itemLists = parseJsonToList(jsonString);

                return itemLists;

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<ResponseCheckItem> result) {
        super.onPostExecute(result);
    }

    private List<ResponseCheckItem> parseJsonToList(String jsonString) {
        List<ResponseCheckItem> itemLists = new ArrayList<>();

        try {
            JSONObject jsonRoot = new JSONObject(jsonString);

            // Ensure the "data" key exists in the JSON object
            if (jsonRoot.has("data")) {
                JSONArray jsonArray = jsonRoot.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.optInt("id");
                    String item_name = jsonObject.optString("item_name");
                    String category_name = jsonObject.optString("category_name");
                    String building_name = jsonObject.optString("building_name");
                    String area_name = jsonObject.optString("area_name");
                    String floor_name = jsonObject.optString("floor_name");
                    String detail_location_name = jsonObject.optString("detail_location_name");
                    int status = jsonObject.optInt("status");
                    String comment = jsonObject.optString("comment");
                    String barcode = jsonObject.optString("barcode");
                    String username = jsonObject.optString("username");

                    ResponseCheckItem temp = new ResponseCheckItem(id, item_name, category_name, building_name, area_name, floor_name, detail_location_name, status, comment, username, barcode);
                    itemLists.add(temp);
                }
            } else {
                Log.e("CheckItemsList::JSON", "'data' key not found in the JSON object");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("CheckItemsList::JSON", "Error parsing JSON: " + e.getMessage());
        }

        return itemLists;
    }

}

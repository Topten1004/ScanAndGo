package com.example.uhf_bt.json;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.uhf_bt.dto.Category;
import com.example.uhf_bt.dto.Item;
import com.example.uhf_bt.dto.LoginVM;
import com.example.uhf_bt.dto.ReadAllItem;
import com.example.uhf_bt.dto.SubCategory;
import com.example.uhf_bt.dto.SubLocation;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
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
import java.util.Arrays;
import java.util.List;

public class JsonTaskGetAllItemList extends AsyncTask<String, String, List<ReadAllItem>> {

    public JsonTaskGetAllItemList() {
        super();
    }

    protected List<ReadAllItem> doInBackground(String... params) {

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

                List<ReadAllItem> allItems = parseJsonToList(jsonString);

                return allItems;

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

    private List<ReadAllItem> parseJsonToList(String jsonString) {
        List<ReadAllItem> categories = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                int subCategoryId = jsonObject.getInt("subcategoryId");
                String name = jsonObject.getString("name");
                String barcode = jsonObject.getString("barcode");

                ReadAllItem category = new ReadAllItem(id, subCategoryId, name, barcode);
                categories.add(category);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    protected void onPostExecute(List<ReadAllItem> result) {
        super.onPostExecute(result);
    }
}
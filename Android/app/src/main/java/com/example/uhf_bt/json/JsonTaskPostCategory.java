package com.example.uhf_bt.json;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.uhf_bt.dto.StatusVM;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class JsonTaskPostCategory extends AsyncTask<String, String, StatusVM> {

    public JsonTaskPostCategory() {
        super();
    }

    protected StatusVM doInBackground(String... params) {

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

            Log.d("status:::", "11111");

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, StandardCharsets.UTF_8));
            writer.write(params[1]);
            writer.flush();

            Log.d("status:::", "22222");

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) buffer.append(line);

            Log.d("status:::", "33333");


            boolean authOk = buffer.length() < 15;
            if(!authOk) authOk = !buffer.substring(0, 15).equals("<!DOCTYPE html>");
            if(authOk){


                Log.d("status:::", "44444");

                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();
                Log.d("Response Code:", String.valueOf(responseCode));
                Log.d("Response Message:", responseMessage);


                Log.d("status:::", buffer.toString());
                Type t = new TypeToken<StatusVM>(){}.getType();
                return new Gson().fromJson(buffer.toString(), t);
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
    protected void onPostExecute(StatusVM result) {
        super.onPostExecute((StatusVM) result);
    }
}

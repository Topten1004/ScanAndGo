package com.example.ScanAndGo.json;

import android.os.AsyncTask;

import com.example.ScanAndGo.dto.ResponseCheckItems;
import com.example.ScanAndGo.dto.ResponseCheckTag;
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

public class JsonTaskCheckItems extends AsyncTask<String, String, ResponseCheckItems> {

    public JsonTaskCheckItems() {
        super();
    }

    protected ResponseCheckItems doInBackground(String... params) {

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

                Type t = new TypeToken<ResponseCheckItems>(){}.getType();
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
    protected void onPostExecute(ResponseCheckItems result) {
        super.onPostExecute((ResponseCheckItems) result);
    }
}

package com.example.uhf_bt.json;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.uhf_bt.dto.MessageVM;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTaskUpdateItem extends AsyncTask<String, String, MessageVM> {

    public JsonTaskUpdateItem() {
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected MessageVM doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);

            // Write the JSON data to the output stream
            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                os.writeBytes(params[1]);
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully updated, handle the response if needed
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                Log.e("error", buffer.toString());

                Type t = new TypeToken<MessageVM>() {}.getType();
                return new Gson().fromJson(buffer.toString(), t);

            } else {
                // Handle the error response if needed
                Log.e("JsonTaskUpdateCategory", "HTTP Error Code: " + responseCode + ", Message: " + responseMessage);
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
    protected void onPostExecute(MessageVM result) {
        super.onPostExecute(result);
        // Handle the result as needed
        if (listener != null) {
            listener.onUpdateComplete();
        }
    }

    public interface OnUpdateCompleteListener {
        void onUpdateComplete();
    }

    private OnUpdateCompleteListener listener;

    public JsonTaskUpdateItem(OnUpdateCompleteListener listener) {
        this.listener = listener;
    }

}

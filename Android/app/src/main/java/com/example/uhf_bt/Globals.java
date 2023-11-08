package com.example.uhf_bt;

import android.app.Application;
import android.os.AsyncTask;

import com.example.uhf_bt.component.Connectivity;
import com.example.uhf_bt.component.NetworkTask;

import java.util.concurrent.ExecutionException;

public class Globals extends Application {

    public static boolean isLogin = false;
    public static String dns = "";
    public static Boolean dispoAPI= false;
    public static String url = "https://api-villedenoumea.scanandgo.nc/";

    public static String apiUrl = url + "api/";

    @SuppressWarnings("deprecation")
    public boolean isNetworkConnected() {
        if(Connectivity.isConnected(getApplicationContext())){
            dispoAPI = false;
            try {
                dispoAPI = new NetworkTask().executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR,dns).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return  false;
    }

}

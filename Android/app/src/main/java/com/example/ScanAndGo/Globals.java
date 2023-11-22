package com.example.ScanAndGo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.ScanAndGo.component.Connectivity;
import com.example.ScanAndGo.component.NetworkTask;
import com.example.ScanAndGo.dto.Category;
import com.example.ScanAndGo.dto.Location;
import com.example.ScanAndGo.dto.SubCategory;
import com.example.ScanAndGo.dto.SubLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Globals extends Application {

    public static boolean isLogin = false;
    public static String dns = "";
    public static Boolean dispoAPI= false;

    public static String url = "https://api-villedenoumea.scanandgo.nc/";
    public static List<Category> categoryLists = new ArrayList<>();
    public static List<Location> locationLists = new ArrayList<>();

    public static List<SubCategory> subCategoryLists = new ArrayList<>();

    public static List<SubLocation> subLocationLists = new ArrayList<>();

    public static String apiUrl = url + "api/";
    public static int categoryId = 0;
    public static int locationId = 0;
    public static int subCategoryId = 0;
    public static int subLocationId = 0;
    public static int checkedItem = 0;

    public static String nowBarCode;

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

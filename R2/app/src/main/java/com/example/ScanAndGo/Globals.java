package com.example.ScanAndGo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.ScanAndGo.component.Connectivity;
import com.example.ScanAndGo.component.NetworkTask;
import com.example.ScanAndGo.dto.Area;
import com.example.ScanAndGo.dto.Category;
import com.example.ScanAndGo.dto.Floor;
import com.example.ScanAndGo.dto.Item;
import com.example.ScanAndGo.dto.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Globals extends Application {

    public static boolean isLogin = false;
    public static String dns = "";
    public static Boolean dispoAPI= false;

    public static String url = "https://api-villedenoumea.scanandgo.nc/";
    public static List<Category> categoryLists = new ArrayList<>();
    public static List<Item> itemLists = new ArrayList<>();
    public static List<Building> buildingList = new ArrayList<>();
    public static List<Area> areaList = new ArrayList<>();
    public static List<Floor> floorList = new ArrayList<>();
    public static List<String> tagsList = new ArrayList<>();
    public static List<String> unknownItems = new ArrayList<>();

    public static String apiUrl = url + "api/";
    public static int categoryId = 0;

    public static String buildingName = "";
    public static String areaName = "";
    public static String floorName = "";

    public static String detailLocationName = "";
    public static int itemId = 0;
    public static int checkedItem = 0;
    public static int buildingId = 0;
    public static int areaId = 0;
    public static int floorId = 0;
    public static int detailLocationId = 0;
    public static String nowBarCode;
    public static String shortCutName = "";

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

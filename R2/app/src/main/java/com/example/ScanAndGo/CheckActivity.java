package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.CheckTagResponse;
import com.example.ScanAndGo.dto.Location;
import com.example.ScanAndGo.dto.LoginVM;
import com.example.ScanAndGo.dto.PostCheckTags;
import com.example.ScanAndGo.dto.PostQRCode;
import com.example.ScanAndGo.json.JsonTaskCheckTag;
import com.example.ScanAndGo.json.JsonTaskGetLocationList;
import com.example.ScanAndGo.json.JsonTaskLogin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckActivity extends BaseActivity{

    private List<ButtonItem> rightItemList = new ArrayList<>();
    private List<ButtonItem> wrongItemList = new ArrayList<>();
    private List<ButtonItem> unknownItemList = new ArrayList<>();
    private List<ButtonItem> missingItemList = new ArrayList<>();

    private ListView rightListView;
    private ListView wrongListView;
    private ListView unknownListView;
    private ListView missingListView;

    private TextView shortCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        rightListView = (ListView)findViewById(R.id.rightItems);
        wrongListView = (ListView)findViewById(R.id.wrongItems);
        unknownListView = (ListView)findViewById(R.id.unknownItems);
        missingListView = (ListView)findViewById(R.id.missingItems);

        shortCut = (TextView)findViewById(R.id.shortCutName);

        shortCut.setText(Globals.shortCutName);
        CallAPI();
    }

    public void CallAPI()
    {
        Globals g = (Globals) getApplication();

        String req = g.apiUrl + "inventory/detect/barcode";

        try {
            PostCheckTags model = new PostCheckTags();

            model.floor_id = Globals.floorId;
            model.barcodes = Globals.tagsList;

            CheckTagResponse response = new CheckTagResponse();


            response = new JsonTaskCheckTag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

            if (response != null) {

                wrongItemList.clear();
                rightItemList.clear();
                missingItemList.clear();
                unknownItemList.clear();

                for (String p : response.unknownLists) {

                    ButtonItem newVM = new ButtonItem(p, 6, 0, true);
                    unknownItemList.add(newVM);
                }

                for (String p : response.rightLists) {

                    ButtonItem newVM = new ButtonItem(p, 6, 0, true);
                    rightItemList.add(newVM);
                }

                for (String p : response.wrongLists) {

                    ButtonItem newVM = new ButtonItem(p, 6, 0, true);
                    wrongItemList.add(newVM);
                }

                for (String p : response.missingLists) {

                    ButtonItem newVM = new ButtonItem(p, 6, 0, true);
                    missingItemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ListItemView rightAdapter = new ListItemView(this, rightItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        rightListView.setAdapter(rightAdapter);

        ListItemView wrongAdapter = new ListItemView(this, wrongItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        wrongListView.setAdapter(wrongAdapter);

        ListItemView unknownAdapter = new ListItemView(this, unknownItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        unknownListView.setAdapter(unknownAdapter);

        ListItemView missingAdapter = new ListItemView(this, missingItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        missingListView.setAdapter(missingAdapter);
    }
}

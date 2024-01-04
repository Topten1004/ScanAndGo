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

public class CheckItemActivity extends BaseActivity{
    private TextView shortCut;

    public int type = 0;

    public String[] barcode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_item);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("barcode") && intent.hasExtra("type"))
        {
            barcode = intent.getStringArrayExtra("barcode");
            type = intent.getIntExtra("type", 0);
        }

        shortCut = (TextView)findViewById(R.id.shortCutName);
        shortCut.setText(Globals.shortCutName);

        CallAPI();
    }

    public void OnBack(View view) {

        startActivityForResult(new Intent(getApplicationContext(), CheckActivity.class), 0);
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

            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

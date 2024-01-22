package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ScanAndGo.dto.PostQRCode;
import com.example.ScanAndGo.dto.QrReturn;
import com.example.ScanAndGo.json.JsonTaskLogin;
import com.example.ScanAndGo.json.JsonTaskQrCode;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ShortCutActivity extends BaseActivity{
    TextView title;
    EditText shortcut;
    Button btnGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);

        title = (TextView) findViewById(R.id.shortCutName);
        shortcut = (EditText) findViewById(R.id.shortCut);
        btnGo = (Button) findViewById(R.id.goShortCut);

        shortcut.requestFocus();
    }


    public void onGoShortCut(View view) {

        PostQRCode model = new PostQRCode();
        model.name = String.valueOf(shortcut.getText());

        String req = Globals.apiUrl + "building/detect-qrcode";

        try {

            QrReturn result = new JsonTaskQrCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    req, model.toJsonString()).get();

            if (result.block_id != -1)
            {
                Globals.shortCutName = String.valueOf(shortcut.getText());

                Globals.buildingId = result.building_id;
                Globals.areaId = result.area_id;
                Globals.floorId = result.floor_id;
                Globals.detailLocationId = result.block_id;

                startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
                Globals.tagsList = new ArrayList<>();

            } else if(result.building_id == -1)
            {
                startActivityForResult(new Intent(getApplicationContext(), BoardBuildingActivity.class), 0);

                showToast("Building doesn't exists");
            } else{

                startActivityForResult(new Intent(getApplicationContext(), BoardBuildingActivity.class), 0);
                showToast("Please input detail location shortcut");
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ScanAndGo.dto.ResponseCheckItems;
import com.example.ScanAndGo.dto.ResponseCheckTag;
import com.example.ScanAndGo.dto.PostCheckItem;
import com.example.ScanAndGo.json.JsonTaskCheckItems;
import com.example.ScanAndGo.json.JsonTaskCheckTag;

import java.util.Arrays;
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

        String req = g.apiUrl + "inventory/barcodelist";

        try {
            PostCheckItem model = new PostCheckItem();

            model.barcodes = Arrays.asList(barcode);

            ResponseCheckItems response = new ResponseCheckItems();

            response = new JsonTaskCheckItems().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

            if (response != null) {

                Log.d("hhhhhhhh", response.data.toString());
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.ScanAndGo;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ScanAndGo.component.ListAddItemView;
import com.example.ScanAndGo.component.ListCheckItemView;
import com.example.ScanAndGo.dto.CheckItem;
import com.example.ScanAndGo.dto.ResponseCheckItem;
import com.example.ScanAndGo.dto.ResponseCheckItems;
import com.example.ScanAndGo.dto.ResponseCheckTag;
import com.example.ScanAndGo.dto.PostCheckItem;
import com.example.ScanAndGo.json.JsonTaskCheckItems;
import com.example.ScanAndGo.json.JsonTaskCheckTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckItemActivity extends BaseActivity{
    private TextView shortCut;

    public int type = 0;

    public String[] barcode ;
    public ListView listView;
    public ArrayList<CheckItem> itemLists = new ArrayList<>();
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

        listView = findViewById(R.id.listCheckItems);

        Log.e("type:::::::::", String.valueOf(type));
        if (type != 0)
        {
            if (type == 6) // right
            {
                listView.setBackgroundColor(Color.parseColor("#64EB8A"));
            } else if(type == 7) {  // wrong
                listView.setBackgroundColor(Color.parseColor("#FAB2B2"));

            } else if(type == 8) {  // missing
                listView.setBackgroundColor(Color.parseColor("#CCCCCC"));

            } else if(type == 9) {  // unknown
                listView.setBackgroundColor(Color.parseColor("#006ED3"));

            }

        }
        ListCheckItemView adapter = new ListCheckItemView(this, itemLists);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
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

            List<ResponseCheckItem> response = new ArrayList<>();

            response = new JsonTaskCheckItems().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

            if (response != null) {

                for(int i = 0; i < response.size(); i++)
                {
                    ResponseCheckItem item = response.get(i);

                    CheckItem temp  = new CheckItem();
                    temp.id  = item.id;
                    temp.detailLocation = item.detail_location_name;
                    temp.floor = item.floor_name;
                    temp.building = item.building_name;
                    temp.area = item.area_name;
                    temp.barCode = item.barcode;
                    temp.name = item.item_name;
                    temp.comment = item.comment;
                    temp.status = item.status;
                    temp.name = item.item_name;

                    itemLists.add(temp);
                }

            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

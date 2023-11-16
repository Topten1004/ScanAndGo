package com.example.uhf_bt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.component.ListAddItemView;
import com.example.uhf_bt.component.ListItemView;
import com.example.uhf_bt.dto.AddItem;
import com.example.uhf_bt.dto.Category;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.dto.LocationItem;
import com.example.uhf_bt.dto.PostCategory;
import com.example.uhf_bt.dto.StatusVM;
import com.example.uhf_bt.dto.SubLocation;
import com.example.uhf_bt.json.JsonTaskGetCategoryList;
import com.example.uhf_bt.json.JsonTaskGetLocationItemList;
import com.example.uhf_bt.json.JsonTaskGetSubLocationList;
import com.example.uhf_bt.json.JsonTaskPostItem;
import com.example.uhf_bt.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardLocationItemActivity extends BaseActivity {

    private ListView listView;
    private List<AddItem> itemList = new ArrayList<>();
    public int locationId = 0;
    public int subLocationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_location_item);


        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "inventory/sublocation/read?id=" + String.valueOf(Globals.subLocationId);

        try {
            itemList.clear();

            List<LocationItem> locationItems = new ArrayList<>();

            locationItems = new JsonTaskGetLocationItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(locationItems);

            if (locationItems != null) {

                for (LocationItem p : locationItems) {

                    AddItem newVM = new AddItem(p.id, 1, p.item_name, p.reg_date, p.barcode, false  );

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listLocationItems);
        ListAddItemView adapter = new ListAddItemView(this, itemList);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }

    public void btnScanItem(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
    }

    public void btnLogOut(View v)
    {
        Globals g = (Globals) getApplication();

        g.isLogin = false;

        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
    }

    public void btnItem(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardCategoryActivity.class), 0);
    }

    public void btnInventory(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardInventoryActivity.class), 0);

    }

    public void btnLocation(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardLocationActivity.class), 0);
    }
}

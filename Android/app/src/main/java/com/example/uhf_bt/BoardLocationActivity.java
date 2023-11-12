package com.example.uhf_bt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.component.ListItemView;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.dto.Location;
import com.example.uhf_bt.dto.PostItem;
import com.example.uhf_bt.dto.StatusVM;
import com.example.uhf_bt.json.JsonTaskGetLocationList;
import com.example.uhf_bt.json.JsonTaskPostCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardLocationActivity extends BaseActivity{

    private ListView listView;
    private TextView addInventoryName;
    private List<ButtonItem> itemList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_location);

        Globals g = (Globals)getApplication();

        addInventoryName = (TextView) findViewById(R.id.txtNameLocation);

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void updateLocation(String text)
    {
        addInventoryName.setText(text);
    }

    public void btnLogOut(View v)
    {
        Globals g = (Globals) getApplication();

        g.isLogin = false;

        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
    }

    public void reCallAPI()
    {
        Globals g = (Globals) getApplication();

        String req = g.apiUrl + "location/read";

        try {
            List<Location> locations = new ArrayList<>();

            locations = new JsonTaskGetLocationList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            if (locations != null) {
                g.locationLists = locations;

                Log.d("count:::", String.valueOf(g.locationLists.size()));

                for (Location p : locations) {

                    ButtonItem newVM = new ButtonItem(p.name, 2, p.id, true);
                    itemList.add(newVM);

                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        listView = findViewById(R.id.listLocationItems);
        ListItemView adapter = new ListItemView(this, itemList, null, this );

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }

    public void btnAddLocation(View v)
    {
        if(addInventoryName.length() > 0 )
        {
            try {
                PostItem model = new PostItem(addInventoryName.getText().toString());
                StatusVM result = new StatusVM();

                String req = Globals.apiUrl + "location/create";

                result = new JsonTaskPostCategory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                if (result != null) {
                    reCallAPI();

                } else {
                    Toast.makeText(getApplicationContext(), "Can't save successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

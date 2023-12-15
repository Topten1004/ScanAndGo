package com.example.ScanAndGo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.Location;
import com.example.ScanAndGo.dto.PostCategory;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskGetLocationList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardLocationActivity extends BaseActivity{

    private ListView listView;
    private TextView addLocationName;
    private List<ButtonItem> itemList = new ArrayList<>();

    private Button addLocation;

    private Button updateLocation;

    public int updateLocationId = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_location);

        Globals g = (Globals)getApplication();

        addLocationName = (TextView) findViewById(R.id.txtNameLocation);

        addLocation = (Button) findViewById(R.id.addLocation);

        updateLocation = (Button) findViewById(R.id.updateLocation);
        updateLocation.setVisibility(View.GONE);

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void updateLocation(String text, int id)
    {
        addLocationName.setText(text);
        updateLocation.setVisibility(View.VISIBLE);
        addLocation.setText("Cancel");

        updateLocationId = id;
    }

    public void btnUpdateLocation(View v)
    {
        if ( updateLocationId > 0 && addLocationName.length() > 0)
        {
            String req = Globals.apiUrl +  "location/update?id=" + updateLocationId;

            PostCategory model = new PostCategory();

            model.name = addLocationName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateLocationId = 0;

            updateLocation.setVisibility(View.GONE);
            addLocationName.setText("");

            reCallAPI();
        }
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

                itemList.clear();
                g.locationLists = locations;

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

        listView = findViewById(R.id.listLocations);
        ListItemView adapter = new ListItemView(this, itemList, null, this, null, null, null);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }

    public void btnAddLocation(View v)
    {
        if (addLocation.getText() == "Add")
        {
            if(addLocationName.length() > 0 )
            {
                try {
                    PostCategory model = new PostCategory(addLocationName.getText().toString());
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "location/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

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
        } else {

            addLocation.setText("Add");
            updateLocation.setVisibility(View.GONE);
            updateLocationId = 0;

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

package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.PostCategory;
import com.example.ScanAndGo.dto.PostSubCategory;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.dto.SubLocation;
import com.example.ScanAndGo.json.JsonTaskGetSubLocationList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardSubLocationActivity extends BaseActivity {

    public int locationId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView subLocationName;
    private Button updateSubLocation;
    private Button addSubLocation;

    public int updateSubLocationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_sub_location);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("locationId")) {

            locationId = intent.getIntExtra("locationId", 0); // 0 is the default value if the key is not found

            Log.d("", String.valueOf(locationId));

            Globals.locationId = locationId;

        }

        updateSubLocation = (Button)findViewById(R.id.updateSubLocation);

        addSubLocation = (Button)findViewById(R.id.addSubLocation);

        updateSubLocation.setVisibility(View.GONE);

        subLocationName = (TextView)findViewById(R.id.txtNameSubLocation);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }


    public void btnAddSubLocation(View v) {

        if(addSubLocation.getText() == "Add")
        {
            if(subLocationName.length() > 0 )
            {
                try {

                    PostSubCategory model = new PostSubCategory(subLocationName.getText().toString(), locationId);
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "sublocation/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                    reCallAPI();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            updateSubLocation.setVisibility(View.GONE);
            addSubLocation.setText("Add");
        }
    }

    public void updateSubLocation(String text, int id)
    {
        updateSubLocation.setVisibility(View.VISIBLE);
        addSubLocation.setText("Cancel");
        subLocationName.setText(text);

        updateSubLocationId = id;
    }

    public void btnUpdateCategory(View v)
    {
        if (updateSubLocationId > 0 && subLocationName.length() > 0)
        {
            String req = Globals.apiUrl +  "sublocation/update?id=" + updateSubLocationId;

            PostCategory model = new PostCategory();

            model.name = subLocationName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateSubLocationId = 0;

            updateSubLocation.setVisibility(View.GONE);
            subLocationName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Log.d("Sub Location:::", String.valueOf(locationId) + " ReCall API");

        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "sublocation/read?id=" + String.valueOf(locationId);

        try {
            itemList.clear();

            List<SubLocation> subLocations = new ArrayList<>();

            subLocations = new JsonTaskGetSubLocationList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(subLocations);

            if (subLocations != null) {

                g.subLocationLists = subLocations;

                for (SubLocation p : subLocations) {

                    Log.d("sub Location Items::", String.valueOf(subLocations.size()));
                    ButtonItem newVM = new ButtonItem(p.getName(), 4, p.id, true);

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listSubLocationItems);
        ListItemView adapter = new ListItemView(this, itemList, null, null , null, this, null);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
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

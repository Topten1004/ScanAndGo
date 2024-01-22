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
import com.example.ScanAndGo.dto.PostDetailLocation;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.dto.SubLocation;
import com.example.ScanAndGo.json.JsonTaskGetSubLocationList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardDetailLocationActivity extends BaseActivity {

    public int floorId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView tvDetailLocationName;
    private Button btnUpdateDetailLocation;
    private Button btnAddDetailLocation;
    public int updateDetailLocationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail_location);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("floorId")) {

            floorId = intent.getIntExtra("floorId", 0); // 0 is the default value if the key is not found

            Log.d("", String.valueOf(floorId));

            Globals.floorId = floorId;

        }

        btnUpdateDetailLocation = (Button)findViewById(R.id.btnUpdateFloor);

        btnAddDetailLocation = (Button)findViewById(R.id.btnAddFloor);

        btnUpdateDetailLocation.setVisibility(View.GONE);

        tvDetailLocationName = (TextView)findViewById(R.id.tvFloorName);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }


    public void OnAddDetailLocation(View v) {

        if(btnAddDetailLocation.getText() == "Add")
        {
            if(tvDetailLocationName.length() > 0 )
            {
                try {

                    PostDetailLocation model = new PostDetailLocation(tvDetailLocationName.getText().toString(), floorId, "");
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "detaillocation/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                    reCallAPI();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            btnUpdateDetailLocation.setVisibility(View.GONE);
            btnAddDetailLocation.setText("Add");
        }
    }

    public void UpdateDetailLocation(String text, int id)
    {
        btnUpdateDetailLocation.setVisibility(View.VISIBLE);
        btnAddDetailLocation.setText("Cancel");
        tvDetailLocationName.setText(text);

        updateDetailLocationId = id;
    }

    public void btnUpdateCategory(View v)
    {
        if (updateDetailLocationId > 0 && tvDetailLocationName.length() > 0)
        {
            String req = Globals.apiUrl +  "detaillocation/update?id=" + updateDetailLocationId;

            PostCategory model = new PostCategory();

            model.name = tvDetailLocationName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateDetailLocationId = 0;

            btnUpdateDetailLocation.setVisibility(View.GONE);
            tvDetailLocationName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Log.d("Area:::", String.valueOf(floorId) + " ReCall API");

        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "detaillocation/read?id=" + String.valueOf(floorId);

        try {
            itemList.clear();

            List<SubLocation> subLocations = new ArrayList<>();

            subLocations = new JsonTaskGetSubLocationList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(subLocations);

            if (subLocations != null) {

                g.subLocationLists = subLocations;

                for (SubLocation p : subLocations) {

                    Log.d("Detail Location Items::", String.valueOf(subLocations.size()));
                    ButtonItem newVM = new ButtonItem(p.getName(), 4, p.id);

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listFloors);
        ListItemView adapter = new ListItemView(this, itemList, null, null , null, null, null, this);

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
        startActivityForResult(new Intent(getApplicationContext(), BoardBuildingActivity.class), 0);
    }
}

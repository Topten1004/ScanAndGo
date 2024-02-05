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
import com.example.ScanAndGo.dto.Building;
import com.example.ScanAndGo.dto.PostBuilding;
import com.example.ScanAndGo.dto.PostCategory;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskGetBuildingList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardBuildingActivity extends BaseActivity{
    private ListView listView;
    private TextView tvBuildingName;
    private List<ButtonItem> itemList = new ArrayList<>();
    private Button btnAddBuilding;
    private Button btnUpdateBuilding;
    public int updateBuildingId = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_building);

        Globals g = (Globals)getApplication();

        tvBuildingName = (TextView) findViewById(R.id.tvBuildingName);

        btnAddBuilding = (Button) findViewById(R.id.btnAddBuilding);

        btnUpdateBuilding = (Button) findViewById(R.id.btnUpdateBuilding);

        btnUpdateBuilding.setVisibility(View.GONE);

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void UpdateBuilding(String text, int id)
    {
        btnAddBuilding.setText(text);
        btnUpdateBuilding.setVisibility(View.VISIBLE);
        btnAddBuilding.setText("Cancel");

        updateBuildingId = id;
    }

    public void OnUpdateBuilding(View v)
    {
        if ( updateBuildingId > 0 && btnAddBuilding.length() > 0)
        {
            String req = Globals.apiUrl +  "building/update?id=" + updateBuildingId;

            PostCategory model = new PostCategory();

            model.name = tvBuildingName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateBuildingId = 0;

            btnUpdateBuilding.setVisibility(View.GONE);
            tvBuildingName.setText("");

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

        String req = g.apiUrl + "building/read";

        try {
            List<Building> buildingList = new ArrayList<>();

            buildingList = new JsonTaskGetBuildingList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            if (buildingList != null) {

                itemList.clear();

                for (Building p : buildingList) {

                    ButtonItem newVM = new ButtonItem(p.name, 3, p.id);
                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listBuildings);
        ListItemView adapter = new ListItemView(this, itemList, null, null, this, null, null, null, null);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }

    public void OnAddBuilding(View v)
    {
        if (btnAddBuilding.getText() == "Add")
        {
            if(tvBuildingName.length() > 0 )
            {
                try {
                    PostBuilding model = new PostBuilding(tvBuildingName.getText().toString());
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "building/create";

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

            btnAddBuilding.setText("Add");
            btnUpdateBuilding.setVisibility(View.GONE);
            updateBuildingId = 0;
        }
    }

    public void btnLocation(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardBuildingActivity.class), 0);
    }
}

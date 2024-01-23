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
import com.example.ScanAndGo.dto.Floor;
import com.example.ScanAndGo.dto.PostCategory;
import com.example.ScanAndGo.dto.PostFloor;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskGetFloorList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardFloorActivity extends BaseActivity {
    public int areaId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView tvFloorName;
    private Button btnUpdateFloor;
    private Button btnAddFloor;
    public int updateFloorId = 0;

    private TextView path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_floor);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("areaId")) {

            areaId = intent.getIntExtra("areaId", 0); // 0 is the default value if the key is not found

            Globals.areaId = areaId;
        }

        btnUpdateFloor = (Button)findViewById(R.id.btnUpdateFloor);

        btnAddFloor = (Button)findViewById(R.id.btnAddFloor);

        btnUpdateFloor.setVisibility(View.GONE);

        tvFloorName = (TextView)findViewById(R.id.tvFloorName);

        path = (TextView)findViewById(R.id.tvLocationFloor);

        path.setText(Globals.buildingName + "/" + Globals.areaName + "/");

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }


    public void OnAddFloor(View v) {

        if(btnAddFloor.getText() == "Add")
        {
            if(tvFloorName.length() > 0 )
            {
                try {

                    PostFloor model = new PostFloor(tvFloorName.getText().toString(), areaId);
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "floor/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                    reCallAPI();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            btnUpdateFloor.setVisibility(View.GONE);
            btnAddFloor.setText("Add");
        }
    }

    public void UpdateFloor(String text, int id)
    {
        btnUpdateFloor.setVisibility(View.VISIBLE);
        btnAddFloor.setText("Cancel");
        tvFloorName.setText(text);

        updateFloorId = id;
    }

    public void OnUpdateFloor(View v)
    {
        if (updateFloorId > 0 && tvFloorName.length() > 0)
        {
            String req = Globals.apiUrl +  "floor/update?id=" + updateFloorId;

            PostCategory model = new PostCategory();

            model.name = tvFloorName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateFloorId = 0;

            tvFloorName.setVisibility(View.GONE);
            tvFloorName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Log.d("Area:::", String.valueOf(areaId) + " ReCall API");

        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "floor/read?id=" + String.valueOf(areaId);

        try {
            itemList.clear();

            List<Floor> floorList = new ArrayList<>();

            floorList = new JsonTaskGetFloorList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(floorList);

            if (floorList != null) {


                for (Floor p : floorList) {

                    ButtonItem newVM = new ButtonItem(p.getName(), 5, p.id);

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listFloors);
        ListItemView adapter = new ListItemView(this, itemList, null, null , null, null, this,null);

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

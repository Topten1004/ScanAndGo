package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.Area;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.PostArea;
import com.example.ScanAndGo.dto.PostCategory;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskGetAreaList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardAreaActivity extends BaseActivity {

    public int buildingId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView tvAreaName;
    private Button btnUpdateArea;
    private Button btnAddArea;
    public int updateAreaId = 0;
    private TextView path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_area);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("buildingId")) {

            buildingId = intent.getIntExtra("buildingId", 0); // 0 is the default value if the key is not found

            Globals.buildingId = buildingId;
        }

        btnUpdateArea = (Button)findViewById(R.id.btnUpdateFloor);

        btnAddArea = (Button)findViewById(R.id.btnAddFloor);

        btnUpdateArea.setVisibility(View.GONE);

        tvAreaName = (TextView)findViewById(R.id.tvFloorName);

        path = (TextView)findViewById(R.id.tvLocationArea);

        path.setText(Globals.buildingName + "/");

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }


    public void btnAddSubLocation(View v) {

        if(tvAreaName.getText() == "Add")
        {
            if(tvAreaName.length() > 0 )
            {
                try {

                    PostArea model = new PostArea(tvAreaName.getText().toString(), buildingId);
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "area/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                    reCallAPI();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            btnUpdateArea.setVisibility(View.GONE);
            btnAddArea.setText("Add");
        }
    }

    public void UpdateArea(String text, int id)
    {
        btnUpdateArea.setVisibility(View.VISIBLE);
        btnAddArea.setText("Cancel");
        tvAreaName.setText(text);

        updateAreaId = id;
    }

    public void OnUpdateArea(View v)
    {
        if (updateAreaId > 0 && tvAreaName.length() > 0)
        {
            String req = Globals.apiUrl +  "area/update?id=" + updateAreaId;

            PostCategory model = new PostCategory();

            model.name = tvAreaName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateAreaId = 0;

            btnUpdateArea.setVisibility(View.GONE);
            tvAreaName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "area/read?id=" + String.valueOf(buildingId);

        try {
            itemList.clear();

            List<Area> areas = new ArrayList<>();

            areas = new JsonTaskGetAreaList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(areas);

            if (areas != null) {


                for (Area p : areas) {

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
        ListItemView adapter = new ListItemView(this, itemList, null, null , null, this, null, null, null);

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

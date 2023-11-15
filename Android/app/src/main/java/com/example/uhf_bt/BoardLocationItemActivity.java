package com.example.uhf_bt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.component.ListItemView;
import com.example.uhf_bt.dto.Category;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.dto.PostCategory;
import com.example.uhf_bt.dto.StatusVM;
import com.example.uhf_bt.json.JsonTaskGetCategoryList;
import com.example.uhf_bt.json.JsonTaskPostItem;
import com.example.uhf_bt.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardLocationItemActivity extends BaseActivity {

    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
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

        String req = g.apiUrl + "category/read";

    }

    public void btnScanItem(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardRegisterLocationItemActivity.class), 0);
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

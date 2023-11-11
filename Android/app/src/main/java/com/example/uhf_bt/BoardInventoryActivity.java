package com.example.uhf_bt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.uhf_bt.component.ListItemView;
import com.example.uhf_bt.dto.Category;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.json.JsonTaskGetCategoryList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardInventoryActivity extends BaseActivity{

    private ListView listView;

    private List<ButtonItem> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_inventory);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        String req = g.apiUrl + "category/read";

        try {
            List<Category> categories = new ArrayList<>();

            categories = new JsonTaskGetCategoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            if (categories != null) {
                g.categoryLists = categories;

                Log.d("count:::", String.valueOf(g.categoryLists.size()));

                for (Category p : categories) {

                    ButtonItem newVM = new ButtonItem(p.getName());
                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        listView = findViewById(R.id.listInventoryItems);
        ListItemView adapter = new ListItemView(this, itemList);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }

    public  void btnLogOut(View v)
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

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
import com.example.ScanAndGo.dto.Item;
import com.example.ScanAndGo.dto.PostCategory;
import com.example.ScanAndGo.dto.PostItem;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskGetItemList;
import com.example.ScanAndGo.json.JsonTaskPostItem;
import com.example.ScanAndGo.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardItemActivity extends BaseActivity {

    public int categoryId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView tvItemName;
    private Button btnUpdateItem;
    private Button btnAddItem;
    public int updateItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("categoryId")) {

            categoryId = intent.getIntExtra("categoryId", 0); // 0 is the default value if the key is not found

            Globals.categoryId = categoryId;
        }

        btnUpdateItem = (Button)findViewById(R.id.btnUpdateItem);

        btnAddItem = (Button)findViewById(R.id.btnAddItem);

        btnUpdateItem.setVisibility(View.GONE);

        tvItemName = (TextView)findViewById(R.id.tvItemName);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void OnAddItem(View v) {

        if(btnAddItem.getText() == "Add")
        {
            if(tvItemName.length() > 0 )
            {
                try {

                    PostItem model = new PostItem(tvItemName.getText().toString(), categoryId, "");
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "item/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                    reCallAPI();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            btnUpdateItem.setVisibility(View.GONE);
            btnAddItem.setText("Add");
        }
    }

    public void UpdateItem(String text, int id)
    {
        btnUpdateItem.setVisibility(View.VISIBLE);
        btnAddItem.setText("Cancel");
        tvItemName.setText(text);

        updateItemId = id;
    }

    public void OnUpdateItem(View v)
    {
        if (updateItemId > 0 && tvItemName.length() > 0)
        {
            String req = Globals.apiUrl +  "item/update?id=" + updateItemId;

            PostCategory model = new PostCategory();

            model.name = tvItemName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateItemId = 0;

            btnUpdateItem.setVisibility(View.GONE);
            tvItemName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "item/read?id=" + String.valueOf(categoryId);
        try {
            itemList.clear();

            List<Item> items = new ArrayList<>();

            items = new JsonTaskGetItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(items);

            if (items != null) {

                g.itemLists = items;

                for (Item p : items) {

                    ButtonItem newVM = new ButtonItem(p.getName(), 2, p.id);

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listSubCategoryItems);
        ListItemView adapter = new ListItemView(this, itemList, null, this , null, null, null, null );

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

    public void btnLocation(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardBuildingActivity.class), 0);
    }

    public void btnInventory(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardInventoryActivity.class), 0);

    }
}

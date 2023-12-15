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

public class BoardCategoryItemActivity extends BaseActivity {

    public int categoryId = 0;

    public int subCategoryId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView itemName;
    private Button updateItem;
    private Button addItem;
    public int updateItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_category_item);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("categoryId") && intent.hasExtra("subCategoryId")) {

            categoryId = intent.getIntExtra("categoryId", 0); // 0 is the default value if the key is not found

            subCategoryId = intent.getIntExtra("subCategoryId", 0); // 0 is the default value if the key is not found

            Log.d("Item Activity:::", String.valueOf(categoryId) + String.valueOf(subCategoryId));

            Globals.categoryId = categoryId;

            Globals.subCategoryId = subCategoryId;

        }

        updateItem = (Button)findViewById(R.id.updateItem);

        addItem = (Button)findViewById(R.id.addItem);

        updateItem.setVisibility(View.GONE);

        itemName = (TextView)findViewById(R.id.txtNameItem);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void btnAddItem(View v) {

        if(addItem.getText() == "Add")
        {
            if(itemName.length() > 0 )
            {
                try {

                    PostItem model = new PostItem(itemName.getText().toString(), Globals.subCategoryId);
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

            updateItem.setVisibility(View.GONE);
            addItem.setText("Add");
        }
    }

    public void updateItem(String text, int id)
    {
        updateItem.setVisibility(View.VISIBLE);
        addItem.setText("Cancel");
        itemName.setText(text);

        updateItemId = id;
    }

    public void btnUpdateItem(View v)
    {
        if (updateItemId > 0 && itemName.length() > 0)
        {
            String req = Globals.apiUrl +  "item/update?id=" + updateItemId;

            PostCategory model = new PostCategory();

            model.name = itemName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateItemId = 0;

            updateItem.setVisibility(View.GONE);
            itemName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Log.d("Item:::", String.valueOf(categoryId) + " ReCall API");

        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "item/read?id=" + String.valueOf(subCategoryId);

        try {
            itemList.clear();

            List<Item> items = new ArrayList<>();

            items = new JsonTaskGetItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(items);

            if (items != null) {

                for (Item p : items) {

                    Log.d("sub Items::", String.valueOf(items.size()));
                    ButtonItem newVM = new ButtonItem(p.getName(), 5, p.id, p.isUsed);

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listItems);
        ListItemView adapter = new ListItemView(this, itemList, null, null , null, null, this  );

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

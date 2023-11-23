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

public class BoardCategoryActivity extends BaseActivity {

    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView categoryName;

    private Button updateCategory;
    private Button addCategory;

    public int updateCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_category);

        updateCategory = (Button)findViewById(R.id.updateCategory);

        addCategory = (Button)findViewById(R.id.btnSearchDevice);

        updateCategory.setVisibility(View.GONE);

        categoryName = (TextView)findViewById(R.id.txtNameCategory);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void btnAddCategory(View v) throws ExecutionException, InterruptedException {

        if(addCategory.getText() == "Add")
        {
            if(categoryName.length() > 0 )
            {
                try {
                    PostCategory model = new PostCategory(categoryName.getText().toString());
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "category/create";

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

            updateCategory.setVisibility(View.GONE);
            addCategory.setText("Add");
            return;

        }
    }

    public void updateCategory(String text, int id)
    {
        updateCategory.setVisibility(View.VISIBLE);
        addCategory.setText("Cancel");
        categoryName.setText(text);

        updateCategoryId = id;
    }

    public void btnUpdateCategory(View v)
    {
        if (updateCategoryId > 0 && categoryName.length() > 0)
        {
            String req = Globals.apiUrl +  "category/update?id=" + updateCategoryId;

            PostCategory model = new PostCategory();

            model.name = categoryName.getText().toString();

            new JsonTaskUpdateItem().execute(req, model.toJsonString());

            updateCategoryId = 0;

            updateCategory.setVisibility(View.GONE);
            categoryName.setText("");

            reCallAPI();
        }
    }

    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "category/read";

        try {
            itemList.clear();

            List<Category> categories = new ArrayList<>();

            categories = new JsonTaskGetCategoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(categories);

            if (categories != null) {
                g.categoryLists = categories;

                for (Category p : categories) {

                    ButtonItem newVM = new ButtonItem(p.getName(), 1, p.id, p.isUsed);

                    itemList.add(newVM);

                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listCategoryItems);
        ListItemView adapter = new ListItemView(this, itemList, this, null, null , null, null);

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

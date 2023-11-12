package com.example.uhf_bt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.component.ListItemView;
import com.example.uhf_bt.dto.Category;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.dto.LoginVM;
import com.example.uhf_bt.dto.PostItem;
import com.example.uhf_bt.dto.StatusVM;
import com.example.uhf_bt.json.JsonTaskGetCategoryList;
import com.example.uhf_bt.json.JsonTaskLogin;
import com.example.uhf_bt.json.JsonTaskPostCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardCategoryActivity extends BaseActivity{

    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();

    private TextView categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_category);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void btnAddCategory(View v) throws ExecutionException, InterruptedException {
        if(categoryName.length() > 0 )
        {
            try {
                PostItem model = new PostItem(categoryName.getText().toString());
                StatusVM result = new StatusVM();

                String req = Globals.apiUrl + "category/create";

                result = new JsonTaskPostCategory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

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
    }

    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

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

        categoryName = findViewById(R.id.txtNameCategory);

        listView = findViewById(R.id.listCategoryItems);
        ListItemView adapter = new ListItemView(this, itemList);

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

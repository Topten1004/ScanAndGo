package com.example.uhf_bt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uhf_bt.component.ListItemView;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.dto.PostCategory;
import com.example.uhf_bt.dto.PostSubCategory;
import com.example.uhf_bt.dto.StatusVM;
import com.example.uhf_bt.dto.SubCategory;
import com.example.uhf_bt.json.JsonTaskGetSubCategoryList;
import com.example.uhf_bt.json.JsonTaskPostItem;
import com.example.uhf_bt.json.JsonTaskUpdateItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoardSubCategoryActivity extends BaseActivity {

    public int categoryId = 0;
    private ListView listView;
    private List<ButtonItem> itemList = new ArrayList<>();
    private TextView categoryName;
    private Button updateCategory;
    private Button addCategory;
    public int updateCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_sub_category);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("categoryId")) {

            categoryId = intent.getIntExtra("categoryId", 0); // 0 is the default value if the key is not found

            Log.d("SubCategory Id", String.valueOf(categoryId));

            Globals.categoryId = categoryId;

        }

        updateCategory = (Button)findViewById(R.id.updateSubCategory);

        addCategory = (Button)findViewById(R.id.addSubCategory);

        updateCategory.setVisibility(View.GONE);

        categoryName = (TextView)findViewById(R.id.txtNameSubCategory);

        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        reCallAPI();
    }

    public void btnAddSubCategory(View v) {

        if(addCategory.getText() == "Add")
        {
            if(categoryName.length() > 0 )
            {
                try {

                    PostSubCategory model = new PostSubCategory(categoryName.getText().toString(), categoryId);
                    StatusVM result = new StatusVM();

                    String req = Globals.apiUrl + "subcategory/create";

                    result = new JsonTaskPostItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

                    reCallAPI();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            updateCategory.setVisibility(View.GONE);
            addCategory.setText("Add");
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
            String req = Globals.apiUrl +  "subcategory/update?id=" + updateCategoryId;

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
        Log.d("Sub Category:::", String.valueOf(categoryId) + " ReCall API");

        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "subcategory/read?id=" + String.valueOf(categoryId);

        try {
            itemList.clear();

            List<SubCategory> subCategories = new ArrayList<>();

            subCategories = new JsonTaskGetSubCategoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(subCategories);

            if (subCategories != null) {

                g.subCategoryLists = subCategories;

                for (SubCategory p : subCategories) {

                    Log.d("sub Items::", String.valueOf(subCategories.size()));
                    ButtonItem newVM = new ButtonItem(p.getName(), 3, p.id, p.isUsed);

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listSubCategoryItems);
        ListItemView adapter = new ListItemView(this, itemList, null, null , this, null, null);

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

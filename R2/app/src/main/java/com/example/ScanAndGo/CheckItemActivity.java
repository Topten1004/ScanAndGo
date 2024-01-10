package com.example.ScanAndGo;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAndGo.component.ListCheckItemView;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.Category;
import com.example.ScanAndGo.dto.CheckItem;
import com.example.ScanAndGo.dto.Item;
import com.example.ScanAndGo.dto.LoginVM;
import com.example.ScanAndGo.dto.PostInventory;
import com.example.ScanAndGo.dto.ResponseCheckItem;
import com.example.ScanAndGo.dto.PostCheckItem;
import com.example.ScanAndGo.json.JsonTaskCheckItems;
import com.example.ScanAndGo.json.JsonTaskCheckTag;
import com.example.ScanAndGo.json.JsonTaskGetCategoryList;
import com.example.ScanAndGo.json.JsonTaskGetItemList;
import com.example.ScanAndGo.json.JsonTaskLogin;
import com.example.ScanAndGo.json.JsonTaskPostInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckItemActivity extends BaseActivity{
    private TextView shortCut;

    public int type = 0;

    public String[] barcode ;
    public ListView listView;

    public int categoryId = 0;

    public int itemId = 0;
    public Spinner categoryList;

    public Spinner itemsList;

    public Button btnAddInventory;
    public ArrayList<CheckItem> itemLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_item);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("barcode") && intent.hasExtra("type"))
        {
            barcode = intent.getStringArrayExtra("barcode");
            type = intent.getIntExtra("type", 0);
        }

        categoryList = (Spinner)findViewById(R.id.categorySelect);
        itemsList = (Spinner)findViewById(R.id.itemSelect);

        btnAddInventory = (Button)findViewById(R.id.btnAddInventory);
        shortCut = (TextView)findViewById(R.id.shortCutName);
        shortCut.setText(Globals.shortCutName);

        if(barcode.length > 0)
        {
            CallAPI();

            listView = findViewById(R.id.listCheckItems);

            ListCheckItemView adapter = new ListCheckItemView(this, itemLists);

            // Set the adapter for the ListView
            listView.setAdapter(adapter);
        }

        if (type != 0)
        {
            if (type == 6) // right
            {

                listView.setBackgroundColor(Color.parseColor("#64EB8A"));
            } else if(type == 7) {  // wrong

                listView.setBackgroundColor(Color.parseColor("#FAB2B2"));
            } else if(type == 8) {  // missing

                listView.setBackgroundColor(Color.parseColor("#CCCCCC"));

            } else if(type == 9 || type == 10) {  // unknown
                listView.setBackgroundColor(Color.parseColor("#006ED3"));
            }
        }
        if(type != 10)
        {
            categoryList.setVisibility(View.GONE);
            itemsList.setVisibility(View.GONE);
            btnAddInventory.setVisibility(View.GONE);
        }

        if (type == 10)
        {
            onReadCategory();
        }
    }

    public void OnBack(View view) {

        startActivityForResult(new Intent(getApplicationContext(), CheckActivity.class), 0);
    }


    public void CallAPI()
    {
        Globals g = (Globals) getApplication();

        String req = g.apiUrl + "inventory/barcodelist";

        try {
            PostCheckItem model = new PostCheckItem();

            model.barcodes = Arrays.asList(barcode);

            List<ResponseCheckItem> response = new ArrayList<>();

            response = new JsonTaskCheckItems().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

            if (response != null) {

                for(int i = 0; i < response.size(); i++)
                {
                    ResponseCheckItem item = response.get(i);

                    CheckItem temp  = new CheckItem();
                    temp.id  = item.id;
                    temp.detailLocation = item.detail_location_name;
                    temp.floor = item.floor_name;
                    temp.building = item.building_name;
                    temp.area = item.area_name;
                    temp.barCode = item.barcode;
                    temp.name = item.item_name;
                    temp.comment = item.comment;
                    temp.status = item.status;
                    temp.name = item.item_name;

                    itemLists.add(temp);
                }

            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void onReadCategory()
    {
        String req = Globals.apiUrl + "category/read";

        Log.d("req::::::::", req);

        try {

            List<Category> categories = new ArrayList<>();

            categories = new JsonTaskGetCategoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(categories);

            if (categories != null) {

                List<Category> finalCategories = categories;
                ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, finalCategories) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                        textView.setText(finalCategories.get(position).getName()); // Assuming getName() returns the category name
                        return textView;
                    }
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        textView.setText(getItem(position).getName()); // Assuming getName() returns the item name
                        return textView;
                    }
                };

                // Specify the layout to use when the list of choices appears
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                categoryList.setAdapter(categoryAdapter);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected category
                Category selectedCategory = (Category) parentView.getItemAtPosition(position);

                // Call API to fetch items based on the selected category
                if (selectedCategory != null) {
                    categoryId = selectedCategory.getId();
                    CallItemsByCategory(categoryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });
    }
    public void onAddInventory(View view)
    {
        if(itemId > 0 && categoryId > 0)
        {
            for(int i = 0; i < itemLists.size(); i++)
            {
                String barcode = itemLists.get(i).barCode;

                PostInventory model = new PostInventory(barcode, 0, "", "", categoryId, itemId, Globals.buildingId, Globals.areaId, Globals.floorId, Globals.detailLocationId);

                Globals g = (Globals)getApplication();

                String req = g.apiUrl + "user/signin";

                try {

                    int result = new JsonTaskPostInventory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            req, model.toJsonString()).get();


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void CallItemsByCategory(int categoryId) {
        Globals g = (Globals) getApplication();

        String req = g.apiUrl + "item/read?id=" + String.valueOf(categoryId);

        Log.e("CallItemsByCategory::", req);

        try {
            List<Item> items = new ArrayList<>();

            items = new JsonTaskGetItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(items);

            if (items != null) {
                List<Item> finalItems = items;
                ArrayAdapter<Item> itemAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_spinner_item, finalItems) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                        textView.setText(finalItems.get(position).getName()); // Assuming getName() returns the item name
                        return textView;
                    }
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        textView.setText(getItem(position).getName()); // Assuming getName() returns the item name
                        return textView;
                    }
                };

                // Specify the layout to use when the list of choices appears
                itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                itemsList.setAdapter(itemAdapter);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        itemsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected category
                Item selectedItem = (Item) parentView.getItemAtPosition(position);

                // Call API to fetch items based on the selected category
                if (selectedItem != null) {
                    itemId = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });
    }
}

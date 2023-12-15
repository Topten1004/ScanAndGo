package com.example.ScanAndGo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.ScanAndGo.dto.ButtonItem;

import java.util.ArrayList;
import java.util.List;

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

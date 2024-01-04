package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.ResponseCheckTag;
import com.example.ScanAndGo.dto.PostCheckTags;
import com.example.ScanAndGo.json.JsonTaskCheckTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckActivity extends BaseActivity{

    private List<ButtonItem> rightItemList = new ArrayList<>();
    private List<ButtonItem> wrongItemList = new ArrayList<>();
    private List<ButtonItem> unknownItemList = new ArrayList<>();
    private List<ButtonItem> missingItemList = new ArrayList<>();

    private String[] rightListValues;
    private String[] wrongListValues;
    private String[] unknownListValues;
    private String[] missingListValues;

    private ListView rightListView;
    private ListView wrongListView;
    private ListView unknownListView;
    private ListView missingListView;

    private TextView shortCut;

    public int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        rightListView = (ListView)findViewById(R.id.rightItems);
        wrongListView = (ListView)findViewById(R.id.wrongItems);
        unknownListView = (ListView)findViewById(R.id.unknownItems);
        missingListView = (ListView)findViewById(R.id.missingItems);

        shortCut = (TextView)findViewById(R.id.shortCutName);

        shortCut.setText(Globals.shortCutName);
        CallAPI();
    }

    public void onCheckRightLocation(View v)
    {
        type = 1;

        Intent intent = new Intent(this, CheckItemActivity.class);

        intent.putExtra("type", 6);
        intent.putExtra("barcode", rightListValues);
        this.startActivity(intent);
    }

    public void onCheckWrongLocation(View v)
    {

        type = 2;

        Intent intent = new Intent(this, CheckItemActivity.class);

        intent.putExtra("type", 7);
        intent.putExtra("barcode", wrongListValues);
        this.startActivity(intent);
    }

    public void onCheckUnknownLocation(View v)
    {

        type = 3;

        Intent intent = new Intent(this, CheckItemActivity.class);

        intent.putExtra("type", 8);
        intent.putExtra("barcode", unknownListValues);
        this.startActivity(intent);
    }

    public void onCheckMissingLocation(View v)
    {
        type = 4;

        Intent intent = new Intent(this, CheckItemActivity.class);

        intent.putExtra("type", 9);
        intent.putExtra("barcode", missingListValues);
        this.startActivity(intent);
    }
    
    public void CallAPI()
    {
        Globals g = (Globals) getApplication();

        String req = g.apiUrl + "inventory/detect/barcode";

        try {
            PostCheckTags model = new PostCheckTags();

            model.floor_id = Globals.floorId;
            model.barcodes = Globals.tagsList;

            ResponseCheckTag response = new ResponseCheckTag();

            response = new JsonTaskCheckTag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, model.toJsonString()).get();

            if (response != null) {

                wrongItemList.clear();
                rightItemList.clear();
                missingItemList.clear();
                unknownItemList.clear();

                for (String p : response.rightLists) {
                    rightListValues = response.rightLists.toArray(new String[0]);
                    ButtonItem newVM = new ButtonItem(p, 6, 0, true);
                    rightItemList.add(newVM);
                }

                for (String p : response.wrongLists) {
                    wrongListValues = response.wrongLists.toArray(new String[0]);
                    ButtonItem newVM = new ButtonItem(p, 7, 0, true);
                    wrongItemList.add(newVM);
                }

                for (String p : response.unknownLists) {
                    unknownListValues = response.unknownLists.toArray(new String[0]);
                    ButtonItem newVM = new ButtonItem(p, 8, 0, true);
                    unknownItemList.add(newVM);
                }

                for (String p : response.missingLists) {
                    missingListValues = response.missingLists.toArray(new String[0]);
                    ButtonItem newVM = new ButtonItem(p, 9, 0, true);
                    missingItemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ListItemView rightAdapter = new ListItemView(this, rightItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        rightListView.setAdapter(rightAdapter);

        ListItemView wrongAdapter = new ListItemView(this, wrongItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        wrongListView.setAdapter(wrongAdapter);

        ListItemView unknownAdapter = new ListItemView(this, unknownItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        unknownListView.setAdapter(unknownAdapter);

        ListItemView missingAdapter = new ListItemView(this, missingItemList, null, null, null, null, null);
        // Set the adapter for the ListView
        missingListView.setAdapter(missingAdapter);
    }
}

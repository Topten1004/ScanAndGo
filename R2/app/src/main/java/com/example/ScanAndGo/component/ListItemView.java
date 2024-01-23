package com.example.ScanAndGo.component;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ScanAndGo.BoardCategoryActivity;
import com.example.ScanAndGo.BoardBuildingActivity;
import com.example.ScanAndGo.BoardDetailLocationActivity;
import com.example.ScanAndGo.BoardFloorActivity;
import com.example.ScanAndGo.BoardItemActivity;
import com.example.ScanAndGo.BoardAreaActivity;
import com.example.ScanAndGo.CheckItemActivity;
import com.example.ScanAndGo.Globals;
import com.example.ScanAndGo.R;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.json.JsonTaskDeleteItem;

import java.util.ArrayList;
import java.util.List;

public class ListItemView extends ArrayAdapter<ButtonItem> {

    public int type;

    public int id;
    public boolean isUsed;
    private BoardCategoryActivity categoryActivity;
    private BoardItemActivity itemActivity;
    private BoardBuildingActivity buildingActivity;
    private BoardAreaActivity areaActivity;
    private BoardDetailLocationActivity detailLocationActivity;
    private BoardFloorActivity floorActivity;

    public ListItemView(@NonNull Context context, @NonNull List<ButtonItem> objects, BoardCategoryActivity categoryActivity, BoardItemActivity itemActivity, BoardBuildingActivity buildingActivity, BoardAreaActivity areaActivity, BoardFloorActivity floorActivity, BoardDetailLocationActivity detailLocationActivity) {

        super(context, 0, objects);

        this.categoryActivity = categoryActivity;
        this.itemActivity = itemActivity;

        this.buildingActivity = buildingActivity;
        this.areaActivity = areaActivity;
        this.floorActivity = floorActivity;
        this.detailLocationActivity = detailLocationActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        ButtonItem item = getItem(position);

        Button mainButton = convertView.findViewById(R.id.mainButton);
        ImageButton editButton = convertView.findViewById(R.id.editButton);
        ImageButton trashButton = convertView.findViewById(R.id.trashButton);
        CheckBox checkBarcode = convertView.findViewById(R.id.checkBarcode);

        if(item.type >= 6)
        {
            editButton.setVisibility(View.GONE);
            trashButton.setVisibility(View.GONE);

            mainButton.setBackgroundResource(android.R.color.transparent);
        }

        if (type < 9)
        {
            checkBarcode.setVisibility(View.GONE);
        } else {
            checkBarcode.setVisibility(View.VISIBLE);
        }

        // Set the data for each view
        mainButton.setText(item.getMainButtonText());

        id = item.id;

        isUsed = item.isUsed;

        type = item.type;

        // Set click listeners for buttons if needed
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == 1)
                {
                    Log.d("mainButton", "11111");
                    Globals.categoryId = item.id;

                    Intent intent = new Intent(getContext(), BoardItemActivity.class);
                    intent.putExtra("categoryId", item.id);
                    getContext().startActivity(intent);

                }
                else if (type == 3)
                {
                    Log.d("mainButton", "22222");

                    Globals.buildingId = item.id;

                    Intent intent = new Intent(getContext(), BoardAreaActivity.class);
                    intent.putExtra("buildingId", item.id);
                    getContext().startActivity(intent);

                } else if (type == 4)
                {
                    Globals.areaId = item.id;

                    Intent intent = new Intent(getContext(), BoardFloorActivity.class);

                    intent.putExtra("areaId", Globals.areaId);

                    getContext().startActivity(intent);
                } else if (type >= 6)           // when user click the check tag part
                {
                    Intent intent = new Intent(getContext(), CheckItemActivity.class);

                    ArrayList<String> barcodes = new ArrayList<>();
                    barcodes.add(item.getMainButtonText());

                    String[] barcode = new String[barcodes.size()];
                    barcode = barcodes.toArray(barcode);

                    intent.putExtra("type", type);
                    intent.putExtra("barcode", barcode);
                    getContext().startActivity(intent);
                }
            }
        });

        // Set click listeners for buttons if needed
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click

                switch(type)
                {
                    case 1:
                        categoryActivity.updateCategory(item.getMainButtonText(), item.id);
                        break;
                    case 2:
                        itemActivity.UpdateItem(item.getMainButtonText(), item.id);
                        break;
                    case 3:
                        buildingActivity.UpdateBuilding(item.getMainButtonText(), item.id);
                        break;
                    case 4:
                        areaActivity.UpdateArea(item.getMainButtonText(), item.id);
                        break;
                    case 5:
                        floorActivity.UpdateFloor(item.getMainButtonText(), item.id);
                        break;
                    case 6:
                        detailLocationActivity.UpdateDetailLocation(item.getMainButtonText(), item.id);
                        break;
                }

            }
        });

        // Set click listeners for buttons if needed
        checkBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click

                if (type == 9)
                {
                    if(checkBarcode.isChecked())
                    {
                        if(!Globals.unknownItems.contains(mainButton.getText()))
                        {
                            Globals.unknownItems.add(mainButton.getText().toString());

                            Log.d("checkbox:::", mainButton.getText().toString());
                        }
                    } else{
                        if(Globals.unknownItems.contains(mainButton.getText()))
                        {
                            Globals.unknownItems.remove(mainButton.getText());
                        }
                    }
                }
            }
        });

        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle trash button click

                if (type == 1)
                {
                    String req = Globals.apiUrl + "category/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    categoryActivity.reCallAPI();

                } else if (type == 2){
                    String req = Globals.apiUrl + "item/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    itemActivity.reCallAPI();
                } else if (type == 3) {
                    String req = Globals.apiUrl + "building/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    buildingActivity.reCallAPI();
                } else if (type == 4) {
                    String req = Globals.apiUrl + "area/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    areaActivity.reCallAPI();
                } else if (type == 5) {
                    String req = Globals.apiUrl + "floor/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    floorActivity.reCallAPI();
                } else if (type == 6) {
                    String req = Globals.apiUrl + "detaillocation/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    detailLocationActivity.reCallAPI();
                }
            }
        });

        return convertView;
    }
}

package com.example.ScanAndGo.component;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ScanAndGo.BoardCategoryActivity;
import com.example.ScanAndGo.BoardCategoryItemActivity;
import com.example.ScanAndGo.BoardLocationActivity;
import com.example.ScanAndGo.BoardLocationItemActivity;
import com.example.ScanAndGo.BoardSubCategoryActivity;
import com.example.ScanAndGo.BoardSubLocationActivity;
import com.example.ScanAndGo.Globals;
import com.example.ScanAndGo.R;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.json.JsonTaskDeleteItem;

import java.util.List;

public class ListItemView extends ArrayAdapter<ButtonItem> {

    public int type;

    public int id;

    public boolean isUsed;

    private BoardCategoryActivity categoryActivity;

    private BoardLocationActivity locationActivity;

    private BoardSubCategoryActivity subCategoryActivity;

    private BoardSubLocationActivity subLocationActivity;

    private BoardCategoryItemActivity itemActivity;
    public ListItemView(@NonNull Context context, @NonNull List<ButtonItem> objects, BoardCategoryActivity categoryActivity, BoardLocationActivity locationActivity, BoardSubCategoryActivity subCategoryActivity, BoardSubLocationActivity subLocationActivity, BoardCategoryItemActivity itemActivity) {

        super(context, 0, objects);

        this.categoryActivity = categoryActivity;
        this.locationActivity = locationActivity;
        this.subCategoryActivity = subCategoryActivity;
        this.subLocationActivity = subLocationActivity;
        this.itemActivity = itemActivity;
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
                    Globals.categoryId = item.id;

                    Intent intent = new Intent(getContext(), BoardSubCategoryActivity.class);
                    intent.putExtra("categoryId", item.id);
                    getContext().startActivity(intent);

                } else if (type == 2)
                {
                    Globals.locationId = item.id;

                    Intent intent = new Intent(getContext(), BoardSubLocationActivity.class);
                    intent.putExtra("locationId", item.id);
                    getContext().startActivity(intent);

                } else if (type == 3)
                {
                    Globals.subCategoryId = item.id;

                    Intent intent = new Intent(getContext(), BoardCategoryItemActivity.class);

                    intent.putExtra("categoryId", Globals.categoryId);
                    intent.putExtra("subCategoryId", Globals.subCategoryId);

                    getContext().startActivity(intent);
                } else if (type == 4)
                {
                    Globals.subLocationId = item.id;

                    Intent intent = new Intent(getContext(), BoardLocationItemActivity.class);

                    intent.putExtra("locationId", Globals.locationId);
                    intent.putExtra("subLocationId", Globals.subLocationId);

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
                        locationActivity.updateLocation(item.getMainButtonText(), item.id);
                        break;
                    case 3:
                        subCategoryActivity.updateCategory(item.getMainButtonText(), item.id);
                        break;
                    case 4:
                        subLocationActivity.updateSubLocation(item.getMainButtonText(), item.id);
                        break;
                    case 5:
                        itemActivity.updateItem(item.getMainButtonText(), item.id);
                        break;
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
                    String req = Globals.apiUrl + "location/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    locationActivity.reCallAPI();
                } else if (type == 3) {
                    String req = Globals.apiUrl + "subcategory/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    Log.e("subCategory:::", String.valueOf(item.id));
                    subCategoryActivity.reCallAPI();
                } else if (type == 4) {
                    String req = Globals.apiUrl + "sublocation/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    Log.e("subLocation:::", String.valueOf(item.id));
                    subLocationActivity.reCallAPI();
                } else if (type == 5) {
                    String req = Globals.apiUrl + "item/delete?id=" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    Log.e("item:::", String.valueOf(item.id));
                    itemActivity.reCallAPI();
                }
            }
        });

        return convertView;
    }
}

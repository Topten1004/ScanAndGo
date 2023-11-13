package com.example.uhf_bt.component;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uhf_bt.BoardCategoryActivity;
import com.example.uhf_bt.BoardLocationActivity;
import com.example.uhf_bt.Globals;
import com.example.uhf_bt.R;
import com.example.uhf_bt.dto.ButtonItem;
import com.example.uhf_bt.json.JsonTaskDeleteItem;

import java.util.List;

public class ListItemView extends ArrayAdapter<ButtonItem> {

    public int type;

    public int id;

    public boolean isUsed;

    private BoardCategoryActivity categoryActivity;

    private BoardLocationActivity locationActivity;

    public ListItemView(@NonNull Context context, @NonNull List<ButtonItem> objects, BoardCategoryActivity categoryActivity, BoardLocationActivity locationActivity) {
        super(context, 0, objects);
        this.categoryActivity = categoryActivity;
        this.locationActivity = locationActivity;

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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
                if (type == 1)
                {
                    categoryActivity.updateCategory(item.getMainButtonText(), item.id);

                } else {

                    locationActivity.updateLocation(item.getMainButtonText(), item.id);
                }
            }
        });

        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle trash button click

                if (type == 1)
                {
                    Log.d("category trash button Clicked", "trash button clicked" + String.valueOf(item.id));

                    String req = Globals.apiUrl + "category/delete?" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    categoryActivity.reCallAPI();

                } else {
                    Log.d("location trash button Clicked", "trash button clicked" + String.valueOf(item.id));

                    String req = Globals.apiUrl + "location/delete?" + String.valueOf(item.id);
                    new JsonTaskDeleteItem().execute(req);

                    locationActivity.reCallAPI();
                }
            }
        });

        return convertView;
    }
}

package com.example.ScanAndGo.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ScanAndGo.Globals;
import com.example.ScanAndGo.R;
import com.example.ScanAndGo.dto.AddItem;

import java.util.List;

public class ListAddItemView extends ArrayAdapter<AddItem> {

    public int id;

    public int type; // 1: Sub location Items 2: Assign Item part
    public String date;

    public String name;
    public String barCode;

    public boolean isCheck;

    public ListAddItemView(@NonNull Context context, @NonNull List<AddItem> objects) {

        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_add_item_view, parent, false);
        }

        AddItem item = getItem(position);

        CheckBox checkBox = convertView.findViewById(R.id.checkAssign);
        TextView date = convertView.findViewById(R.id.txtDate);
        TextView barCode = convertView.findViewById(R.id.txtBarCode);
        TextView name = convertView.findViewById(R.id.txtName);

        // Set the data for each view
        name.setText(item.getName());
        barCode.setText(item.barCode);
        date.setText(item.date);

        id = item.id;
        isCheck = item.isCheck;
        type = item.type;

        // Explicitly set the state of the checkbox
        checkBox.setChecked(isCheck);

        if (type == 1) {
            checkBox.setVisibility(View.GONE);
        }
        if (type == 2) {
            date.setVisibility(View.GONE);
        }

        // Set click listeners for buttons if needed
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Update the state of the item when the checkbox is clicked
                item.isCheck = checkBox.isChecked();
                Globals.checkedItem = item.id;
            }
        });

        return convertView;
    }
}

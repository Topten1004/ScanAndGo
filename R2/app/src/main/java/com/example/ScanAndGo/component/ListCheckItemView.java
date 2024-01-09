package com.example.ScanAndGo.component;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ScanAndGo.R;
import com.example.ScanAndGo.dto.CheckItem;

import java.util.List;

public class ListCheckItemView extends ArrayAdapter<CheckItem> {

    public int id;

    public int type; // 1: Sub location Items 2: Assign Item part

    public String date;

    public String name;
    public String barCode;
    public boolean isCheck;
    public int status;
    public String comment;
    public String building;
    public String area;
    public String floor;
    public String detailLocation;

    public ListCheckItemView(@NonNull Context context, @NonNull List<CheckItem> objects) {

        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_check_item_view, parent, false);
        }

        CheckItem item = getItem(position);

        String locationName = item.building + "/" + item.area + "/" + item.floor + "/" + item.detailLocation;

        TextView location = convertView.findViewById(R.id.txtLocation);
        TextView barCode = convertView.findViewById(R.id.txtCheckItemBarCode);
        TextView name = convertView.findViewById(R.id.txtName);
        TextView comment = convertView.findViewById(R.id.txtComment);

        Button main = convertView.findViewById(R.id.mainButton);

        ImageButton bad = convertView.findViewById(R.id.btnStatusBad);
        ImageButton normal = convertView.findViewById(R.id.btnStatusNormal);
        ImageButton good = convertView.findViewById(R.id.btnStatusGood);

        bad.setEnabled(true);
        normal.setEnabled(true);
        good.setEnabled(true);

        bad.setBackgroundColor(Color.GRAY);
        normal.setBackgroundColor(Color.GRAY);
        good.setBackgroundColor(Color.GRAY);

        if(item.status == 1)
        {
            normal.setEnabled(false);
            good.setEnabled(false);

            bad.setBackgroundColor(Color.WHITE);
        }
        if(item.status == 2)
        {
            bad.setEnabled(false);
            good.setEnabled(false);

            normal.setBackgroundColor(Color.WHITE);
        }
        if(item.status == 3)
        {
            normal.setEnabled(false);
            bad.setEnabled(false);

            good.setBackgroundColor(Color.WHITE);
        }

        // Set the data for each view
        location.setText(locationName);
        name.setText(item.getName());
        barCode.setText(item.barCode);
        comment.setText(item.comment);

        id = item.id;
        isCheck = item.isCheck;
        type = item.type;

        return convertView;
    }
}
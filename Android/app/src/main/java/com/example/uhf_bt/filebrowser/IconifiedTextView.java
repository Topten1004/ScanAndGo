package com.example.uhf_bt.filebrowser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconifiedTextView extends LinearLayout {
    // 一个文件包括文件名和图表
    // 采用一个垂直线性布局
    private TextView mText = null;
    private ImageView mIcon = null;

    public IconifiedTextView(Context context, com.example.uhf_bt.filebrowser.IconifiedText aIconifiedText) {
        super(context);
        // 设置布局方式
        this.setOrientation(HORIZONTAL);
        mIcon = new ImageView(context);
        // 设置ImageView为文件的图标
        mIcon.setImageDrawable(aIconifiedText.getIcon());
        // 设置图标在该布局中的填充位置
        mIcon.setPadding(6, 14, 6, 14);
        // 将ImageView即图表添加到该布局中
        addView(mIcon, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        // 设置文件名、填充方式、字体大小
        mText = new TextView(context);
        mText.setText(aIconifiedText.getText());
        mText.setPadding(4, 6, 4, 6);
        mText.setTextSize(24);
        mText.setWidth(LayoutParams.WRAP_CONTENT);
        mText.setHeight(LayoutParams.WRAP_CONTENT);
        // 将文件名添加到布局中
        addView(mText, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    // 设置文件名
    public void setText(String words) {
        mText.setText(words);
    }

    // 设置图标
    public void setIcon(Drawable bullet) {
        mIcon.setImageDrawable(bullet);
    }
}

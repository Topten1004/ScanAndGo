package com.example.ScanAndGo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShortCutActivity extends BaseActivity{

    TextView title;
    EditText shortcut;

    Button btnGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);

        title = (TextView) findViewById(R.id.shortCutName);
        shortcut = (EditText) findViewById(R.id.shortCut);
        btnGo = (Button) findViewById(R.id.goShortCut);
    }


    public void onGoShortCut(View view) {
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);

    }
}

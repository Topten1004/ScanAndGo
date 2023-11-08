package com.example.uhf_bt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BoardActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);
    }

    public void btnLogOut(View view) {

        Globals g = (Globals)getApplication();

        g.isLogin = false;
        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
    }
}

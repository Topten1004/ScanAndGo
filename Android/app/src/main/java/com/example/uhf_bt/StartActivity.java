package com.example.uhf_bt;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        findViewById(R.id.btBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra("BTMode",true);
                StartActivity.this.startActivity(intent);
                StartActivity.this.finish();


            }
        });
        findViewById(R.id.btSerialPort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra("BTMode",false);
                StartActivity.this.startActivity(intent);
                StartActivity.this.finish();
            }
        });

        Intent intent=new Intent(StartActivity.this,MainActivity.class);
        intent.putExtra("BTMode",true);
        StartActivity.this.startActivity(intent);
        StartActivity.this.finish();
    }

}

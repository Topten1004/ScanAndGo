package com.example.uhf_bt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.dto.Login;
import com.example.uhf_bt.dto.LoginVM;
import com.example.uhf_bt.json.JsonTaskLogin;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends BaseActivity{

    TextView userName = null;

    TextView password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        userName = (TextView) findViewById(R.id.txtUserName);
        password = (TextView) findViewById(R.id.txtPassword);

    }

        public void btnLogin(View v)
        {
            if (userName.getText().length() > 0 && password.getText().length() > 0)
            {
                Login model = new Login();

                model.username = userName.getText().toString();
                model.password = password.getText().toString();

                String req = "https://api-villedenoumea.scanandgo.nc/api/" + "user/signin";

                try {

                    LoginVM result = new LoginVM();

                    result = new JsonTaskLogin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            req,
                            new Gson().toJson(model)).get();


                    if (result != null) {
                        Log.d("success", "Login Success");
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown code", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
}

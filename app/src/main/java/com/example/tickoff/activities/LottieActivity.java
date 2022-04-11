package com.example.tickoff.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.tickoff.R;
import com.example.tickoff.RequestHandler;
import com.example.tickoff.RequestTask;
import com.example.tickoff.Response;

import org.json.JSONObject;

import java.util.HashMap;

public class LottieActivity extends AppCompatActivity implements RequestTask.OutResponse {

    private static int SPLASH_TIME_OUT = 2000;
    private SharedPreferences user;

    int main = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestHandler.setup();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lottie);

        getSupportActionBar().hide();

        SplashScreenActivation();


    }

    private void SplashScreenActivation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent changeActivity;
                user = getSharedPreferences("TickOff", Context.MODE_PRIVATE);
                if (user.contains("login")){
                    SharedPreferences restartUser = getSharedPreferences("TickOff", Context.MODE_PRIVATE);
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("email_or_username", restartUser.getString("login", ""));
                    dataMap.put("password", restartUser.getString("pwd", ""));
                    JSONObject dataJSON = new JSONObject(dataMap);
                    RequestTask login = new RequestTask(LottieActivity.this,"login", "POST", dataJSON.toString());
                    login.execute();
                    changeActivity = new Intent(LottieActivity.this, MainActivity.class);
                }else{
                    changeActivity = new Intent(LottieActivity.this, LoginActivity.class);
                }
                startActivity(changeActivity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    public void response(Response response) {
        //TODO: hibakezelés
        Log.d("RES", response.getContent());
    }
}
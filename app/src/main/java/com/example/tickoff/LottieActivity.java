package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class LottieActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private SharedPreferences user;

    int main = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
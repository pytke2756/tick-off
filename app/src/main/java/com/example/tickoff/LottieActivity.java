package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LottieActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    int main = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        getSupportActionBar().hide();

        SplashScreenActivation();
    }

    private void SplashScreenActivation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent changeActivity;
                if (main == 1){
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
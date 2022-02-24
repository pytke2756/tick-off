package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button regBtn;
    private TextView forgotPsw;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        init();
        
        forgotPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgottenPsw = new Intent(LoginActivity.this, ForgottenActivity.class);
                startActivity(forgottenPsw);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(reg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        check();
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void check(){
        if (!isNetworkConnected()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Intent panel = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                startActivityForResult(panel, 0);
            }
            else{
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    private void init(){
        regBtn = findViewById(R.id.reg_btn);
        forgotPsw = findViewById(R.id.forgot_psw);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }
}
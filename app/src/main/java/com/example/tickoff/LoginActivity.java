package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton regBtn;
    private MaterialButton loginBtn;
    private AppCompatTextView forgotPswTv;
    private AppCompatTextView emailUserErrorTv;
    private AppCompatTextView pswErrorTv;
    private TextInputEditText userEmailEt;
    private TextInputEditText userPswEt;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        init();
        
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameMail = userEmailEt.getText().toString();
                String userPws = userPswEt.getText().toString();
                emailUserErrorTv.setText("");
                pswErrorTv.setText("");
                boolean isValid = true;
                
                if (userNameMail.isEmpty()){
                    isValid = false;
                    emailUserErrorTv.setText(R.string.user_email_error);
                }

                if (userPws.isEmpty()){
                    isValid = false;
                    pswErrorTv.setText(R.string.psw_error);
                }

                if (isValid){
                    //TODO: api request bejelentkezés küldése
                    /*
                    * Ha a request hibával tér vissza akkor az error textekbe bele írni!
                    * */
                }

            }
        });
        
        forgotPswTv.setOnClickListener(new View.OnClickListener() {
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
        regBtn = findViewById(R.id.login_reg_btn);
        loginBtn = findViewById(R.id.login_login_btn);
        userEmailEt = findViewById(R.id.login_email_user_et);
        userPswEt = findViewById(R.id.login_psw_et);
        forgotPswTv = findViewById(R.id.login_forgot_psw);
        emailUserErrorTv = findViewById(R.id.login_email_user_error_tv);
        pswErrorTv = findViewById(R.id.login_psw_error_tv);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }
}
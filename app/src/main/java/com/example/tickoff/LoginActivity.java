package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements RequestTask.OutResponse{

    private MaterialButton regBtn;
    private MaterialButton loginBtn;
    private AppCompatTextView forgotPswTv;
    private AppCompatTextView emailUserErrorTv;
    private AppCompatTextView pswErrorTv;
    private TextInputEditText userEmailEt;
    private TextInputEditText userPswEt;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        init();
        
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameMail = userEmailEt.getText().toString();
                String userPws = userPswEt.getText().toString();
                errorReset();
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
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("email_or_username", userNameMail);
                    dataMap.put("password", userPws);
                    JSONObject dataJSON = new JSONObject(dataMap);
                    RequestTask login = new RequestTask(LoginActivity.this,"http://10.0.2.2:5000/login", "POST", dataJSON.toString());
                    login.execute();
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

    private void errorReset() {
        emailUserErrorTv.setText("");
        pswErrorTv.setText("");
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

        sharedpreferences = getSharedPreferences("TickOff", Context.MODE_PRIVATE);
    }

    @Override
    public void response(Response response) {
        if (response.getResponseCode() >= 400){
            //TODO: backendend a hibákat visszaadni
            emailUserErrorTv.setText(response.getContent());
        }
        else if (response.getResponseCode() == 200){
            //TODO: a visszaadott tokent/id-t SharedPreferencesbe menteni
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("login", userEmailEt.getText().toString());
            editor.putString("pwd", userPswEt.getText().toString());
            editor.commit();
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }
    }
}
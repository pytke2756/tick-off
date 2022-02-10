package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button regBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(reg);
            }
        });
    }

    private void init(){
        regBtn = findViewById(R.id.reg_btn);
    }
}
package com.example.tickoff.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tickoff.R;

public class ForgottenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);

        getSupportActionBar().hide();
    }
}
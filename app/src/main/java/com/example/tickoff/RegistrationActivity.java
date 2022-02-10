package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity {

    private Button regFrgmtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        regFrgmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OtherRegFragment()).commit();
                regFrgmtBtn.setText("Regisztráció");
            }
        });
    }

    private void init(){
        regFrgmtBtn = findViewById(R.id.reg_frgmt_btn);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BasicRegFragment()).commit();
    }
}
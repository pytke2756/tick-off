package com.example.tickoff.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.tickoff.R;
import com.example.tickoff.RequestTask;
import com.example.tickoff.Response;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgottenActivity extends AppCompatActivity implements RequestTask.OutResponse {
    private TextInputEditText forgottenEmail;
    private AppCompatTextView forgottenEmailError;
    private MaterialButton forgottenDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_forgotten);

        getSupportActionBar().hide();
        init();

        forgottenDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorReset();
                String email = forgottenEmail.getText().toString().trim();
                boolean isValid = true;
                if (email.isEmpty()){
                    forgottenEmailError.setText(R.string.empty_input_error);
                    isValid = false;
                }
                if (isValid){
                    String forgottenString = "";
                    try {
                        forgottenString = new JSONObject()
                                .put("email", email)
                                .toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestTask forgotten = new RequestTask(ForgottenActivity.this,"password-reset-request", "POST", forgottenString);
                    forgotten.execute();
                }

            }
        });


    }

    private void init(){
        forgottenEmail = findViewById(R.id.forgotten_email);
        forgottenEmailError = findViewById(R.id.forgotten_email_error);
        forgottenDone = findViewById(R.id.forgotten_done);
    }

    @Override
    public void response(Response response) {
        if (response.getResponseCode() == 500){
            forgottenEmailError.setText("Hibás e-mail cím");
        }
        if (response.getResponseCode() == 200){
            emailSentDialog();
        }
    }

    private void errorReset(){
        forgottenEmailError.setText("");
    }
    
    private void emailSentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("E-mail címedre küldött linkre kattintva új jelszót kapsz, amivel majd\n"+
                "bejelentkezhetsz.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent login = new Intent(ForgottenActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
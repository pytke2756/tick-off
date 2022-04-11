package com.example.tickoff.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tickoff.PswCheck;
import com.example.tickoff.R;
import com.example.tickoff.RequestTask;
import com.example.tickoff.Response;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthLevel;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RequestTask.OutResponse {

    private MaterialButton regRegBtn;
    private TextInputEditText regEmailEt;
    private TextInputEditText regPwdEt;
    private TextInputEditText regPwdAgainEt;
    private TextInputEditText regFirstnameEt;
    private TextInputEditText regLastnameEt;
    private TextInputEditText regBirthEt;
    private TextInputEditText regUsernameEt;
    private AppCompatTextView regEmailErrorTv;
    private AppCompatTextView pwdMatchErrorTv;
    private AppCompatTextView regBirthErrorTv;
    private AppCompatTextView regLastnameErrorTv;
    private AppCompatTextView regFirstnameErrorTv;
    private AppCompatTextView regUsernameErrorTv;
    private PasswordStrengthMeter meter;
    private PasswordStrengthLevel[] strengthLevels;

    private boolean pwdIsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        init();


        //lehet nem ez az event kell majd, de mvp-re jó
        regPwdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    String pwd = regPwdEt.getText().toString().trim();
                    String pwdAgain = regPwdAgainEt.getText().toString().trim();
                    if (!pwd.equals(pwdAgain)){
                        pwdMatchErrorTv.setText("Nem egyezik a két jelszó!");
                    }
                    else{
                        pwdMatchErrorTv.setText("");
                    }
                }
            }
        });

        regPwdAgainEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    String pwd = regPwdEt.getText().toString().trim();
                    String pwdAgain = regPwdAgainEt.getText().toString().trim();
                    if (!pwd.equals(pwdAgain)){
                        pwdMatchErrorTv.setText("Nem egyezik a két jelszó!");
                    }
                    else{
                        pwdMatchErrorTv.setText("");
                    }
                }
            }
        });

        meter.setPasswordStrengthCalculator(new PasswordStrengthCalculator() {
            @Override
            public int calculatePasswordSecurityLevel(String password) {
                if (password.length() > 10 && PswCheck.pwdSpecialCharacterCheck(password) &&
                        PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password) && PswCheck.pwdDigitCheck(password)){
                    return 5;
                }
                else if(password.length() > 10 && PswCheck.pwdDigitCheck(password)&&
                        PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)){
                    return 4;
                }
                else if (password.length() > 9 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && !PswCheck.pwdSpecialCharacterCheck(password)){
                    return 4;
                }
                else if (password.length() > 9 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && PswCheck.pwdSpecialCharacterCheck(password)){
                    return 4;
                }
                else if (password.length() > 7 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && PswCheck.pwdSpecialCharacterCheck(password)){
                    return 4;
                }
                else if (password.length() > 7 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && !PswCheck.pwdSpecialCharacterCheck(password)){
                    return 3;
                }
                else if(password.length() < 8 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && !PswCheck.pwdSpecialCharacterCheck(password)){
                    return 2;
                }
                else if(password.length() < 8 && !PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && !PswCheck.pwdSpecialCharacterCheck(password)){
                    return 2;
                }
                else if(password.length() < 8 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && !PswCheck.pwdDigitCheck(password) && !PswCheck.pwdSpecialCharacterCheck(password)){
                    return 2;
                }
                else if(password.length() < 8 && PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && PswCheck.pwdSpecialCharacterCheck(password)){
                    return 2;
                }
                else if(password.length() < 8 && !PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && PswCheck.pwdDigitCheck(password) && PswCheck.pwdSpecialCharacterCheck(password)){
                    return 2;
                }
                else if (password.length() > 3 && !PswCheck.pwdUppercaseCheck(password) && PswCheck.pwdLowercaseCheck(password)
                        && !PswCheck.pwdDigitCheck(password) && !PswCheck.pwdSpecialCharacterCheck(password)){
                    pwdIsValid = false;
                    return 2;
                }else if (password.length() >2){
                    pwdIsValid = false;
                    return 1;
                }else{
                    pwdIsValid = false;
                    return 0;
                }
            }

            @Override
            public int getMinimumLength() {
                return 3;
            }

            @Override
            public boolean passwordAccepted(int level) {
                return level>2;
            }

            @Override
            public void onPasswordAccepted(String password) {
                pwdIsValid = true;
            }
        });

        regBirthEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthPickerDialog();
            }
        });

        regRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;

                cleanErrorMessages();

                String email = regEmailEt.getText().toString().trim();
                String username = regUsernameEt.getText().toString().trim();
                String pwd = regPwdEt.getText().toString().trim();
                String firstname = regFirstnameEt.getText().toString().trim();
                String lastname = regLastnameEt.getText().toString().trim();
                String birthDay = regBirthEt.getText().toString();

                if (email.isEmpty()){
                    isValid = false;
                    regEmailErrorTv.setText(R.string.empty_input_error);
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    isValid = false;
                    regEmailErrorTv.setText("Valós email címet kell megadni!");
                }

                if (username.isEmpty()){
                    isValid = false;
                    regUsernameErrorTv.setText(R.string.empty_input_error);
                }

                if (pwd.isEmpty()){
                    isValid = false;
                    pwdMatchErrorTv.setText(R.string.empty_input_error);
                }
                if (!pwdIsValid){
                    Toast.makeText(RegistrationActivity.this, "Nem felel meg a jelszavad", Toast.LENGTH_SHORT).show();
                }
                if (firstname.isEmpty()){
                    isValid = false;
                    //TODO: kicsúszik a szöveg
                    regFirstnameErrorTv.setText(R.string.empty_input_error);
                }
                if (lastname.isEmpty()){
                    isValid = false;
                    //TODO: kicsúszik a szöveg
                    regLastnameErrorTv.setText(R.string.empty_input_error);
                }
                if (birthDay.isEmpty()){
                    isValid = false;
                    regBirthErrorTv.setText(R.string.empty_input_error);
                }
                if (isValid){
                    //TODO: request api, regisztráció küldése, ha hiba van akkor mehetnek az errorokba
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("email", email);
                    dataMap.put("username", username);
                    dataMap.put("password", pwd);
                    dataMap.put("password_again", regPwdAgainEt.getText().toString());
                    dataMap.put("firstName", firstname);
                    dataMap.put("lastName", lastname);
                    String[] date = birthDay.split("/");
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MONTH, Integer.parseInt(date[0]));
                    c.set(Calendar.DATE, Integer.parseInt(date[1]));
                    c.set(Calendar.YEAR, Integer.parseInt(date[2]));
                    Date d = c.getTime();
                    dataMap.put("born_date", String.valueOf(Math.floor(d.getTime()/1000)));
                    JSONObject dataJSON = new JSONObject(dataMap);
                    RequestTask registration = new RequestTask(RegistrationActivity.this, "register", "POST", dataJSON.toString());
                    registration.execute();
                }
            }
        });
    }

    private void birthPickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.datePickerTheme,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }

    private void cleanErrorMessages(){
        regEmailErrorTv.setText("");
        regUsernameErrorTv.setText("");
        pwdMatchErrorTv.setText("");
        regBirthErrorTv.setText("");
        regLastnameErrorTv.setText("");
        regFirstnameErrorTv.setText("");
    }

    private void init(){
        regRegBtn = findViewById(R.id.reg_reg_btn);
        regEmailEt = findViewById(R.id.reg_email_et);
        regPwdEt = findViewById(R.id.reg_pwd_et);
        regUsernameEt = findViewById(R.id.reg_username_et);

        regPwdAgainEt = findViewById(R.id.reg_pwd_again_et);
        regFirstnameEt = findViewById(R.id.reg_firstname_et);
        regLastnameEt = findViewById(R.id.reg_lastname_et);
        regBirthEt = findViewById(R.id.reg_birth_et);

        regEmailErrorTv = findViewById(R.id.reg_email_error_tv);
        pwdMatchErrorTv = findViewById(R.id.pwd_match_error_tv);
        regBirthErrorTv = findViewById(R.id.reg_birth_error_tv);
        regLastnameErrorTv = findViewById(R.id.reg_lastname_error_tv);
        regFirstnameErrorTv = findViewById(R.id.reg_firstname_error_tv);
        regUsernameErrorTv = findViewById(R.id.reg_username_error_tv);

        meter = findViewById(R.id.pwd_meter);
        meter.setEditText(regPwdEt);

        pwdIsValid = false;

        strengthLevels = new PasswordStrengthLevel[]{
                new PasswordStrengthLevel("Túl rövid", android.R.color.darker_gray), //0
                new PasswordStrengthLevel("Gyenge", android.R.color.holo_red_dark), //1
                new PasswordStrengthLevel("Közepes", android.R.color.holo_orange_dark), //2
                new PasswordStrengthLevel("Jó", android.R.color.holo_orange_light), //3
                new PasswordStrengthLevel("Erős", android.R.color.holo_blue_light), //4
                new PasswordStrengthLevel("Nagyon erős", android.R.color.holo_green_dark)}; //5

        meter.setStrengthLevels(strengthLevels);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        regBirthEt.setText(date);
    }

    @Override
    public void response(Response response) {
        if (response.getResponseCode() >= 400){
            //TODO: Errorokat visszaadni
            Log.d("ERROR", response.getContent());
        }
        else{
            Log.d("SIKER", response.getContent());
            Intent login = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }
    }
}
package com.example.tickoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.regex.Pattern;

import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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
                if (password.length() > 12 && pwdSpecialCharacterCheck(password) &&
                        pwdUppercaseCheck(password) && pwdLowercaseCheck(password) && pwdDigitCheck(password)){
                    return 5;
                }else if(password.length() > 12 && pwdDigitCheck(password)&&
                        pwdUppercaseCheck(password) && pwdLowercaseCheck(password)){
                    return 4;
                }else if (password.length() > 12 && pwdUppercaseCheck(password) && pwdLowercaseCheck(password)){
                    return 3;
                }
                else if (password.length() > 8 && pwdUppercaseCheck(password)){
                    pwdIsValid = false;
                    return 2;
                }else if (password.length() > 6){
                    pwdIsValid = false;
                    return 1;
                }else{
                    pwdIsValid = false;
                    return 0;
                }
            }

            @Override
            public int getMinimumLength() {
                return 4;
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
                    Toast.makeText(RegistrationActivity.this, "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
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

    private boolean pwdDigitCheck(String password){
        Pattern digit = Pattern.compile("[0-9]");
        if (digit.matcher(password).find()){
            return true;
        }
        return false;
    }

    private boolean pwdLowercaseCheck(String password){
        Pattern lowercase = Pattern.compile("[a-z]");
        if (lowercase.matcher(password).find()){
            return true;
        }
        return false;
    }

    private boolean pwdUppercaseCheck(String password){
        Pattern uppercase = Pattern.compile("[A-Z]");
        if (uppercase.matcher(password).find()){
            return true;
        }
        return false;
    }

    private boolean pwdSpecialCharacterCheck(String password){
        String[] specialCharacters = {"!", "\"", "#" , "$", "%", "&", "\\'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";",
                "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"};

        for (int i = 0; i < specialCharacters.length; i++) {
            if (password.contains(specialCharacters[i])){
                return true;
            }
        }
        return false;
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
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        regBirthEt.setText(date);
    }
}
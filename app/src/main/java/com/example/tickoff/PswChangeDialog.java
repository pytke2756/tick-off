package com.example.tickoff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthLevel;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;


public class PswChangeDialog extends AppCompatDialogFragment{
    private TextInputEditText passwordChangeDialogPassword;
    private TextInputEditText passwordChangeDialogNewPassword;
    private TextInputEditText passwordChangeDialogNewPasswordAgain;
    private AppCompatTextView passwordChangeDialogPasswordError;
    private AppCompatTextView passwordChangeDialogNewPasswordError;
    private AppCompatTextView passwordChangeDialogNewPasswordAgainError;
    private MaterialButton passwordChangeDialogEditBtn;
    private MaterialButton passwordChangeDialogCancelBtn;
    private PasswordStrengthMeter meter;
    private PasswordStrengthLevel[] strengthLevels;

    private boolean pwdIsValid;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.password_change_dialog,null);
        init(view);
        builder.setView(view);
        passwordChangeDialogEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorReset();
                String psw = passwordChangeDialogPassword.getText().toString().trim();
                String newPsw = passwordChangeDialogNewPassword.getText().toString().trim();
                String newPswAgain = passwordChangeDialogNewPasswordAgain.getText().toString().trim();
                boolean isValid = true;
                SharedPreferences login = getContext().getSharedPreferences("TickOff", Context.MODE_PRIVATE);
                if (psw.isEmpty()){
                    passwordChangeDialogPasswordError.setText(R.string.empty_input_error);
                    isValid = false;
                }

                if (!psw.equals(login.getString("pwd", ""))){
                    passwordChangeDialogPasswordError.setText("Hibás jelszó");
                }
                if (newPsw.isEmpty()){
                    passwordChangeDialogNewPasswordError.setText(R.string.empty_input_error);
                    isValid = false;
                }
                if (newPswAgain.isEmpty()){
                    passwordChangeDialogNewPasswordAgainError.setText(R.string.empty_input_error);
                    isValid = false;
                }
                if (!newPsw.equals(newPswAgain)){
                    passwordChangeDialogNewPasswordError.setText("A két jelszó nem egyezik");
                    isValid = false;
                }
                if (!pwdIsValid){
                    passwordChangeDialogNewPasswordError.setText("Legalább a jó szintet el kell érned");
                }
                if (isValid && pwdIsValid){
                    String pswChangeString = "";
                    try {
                        pswChangeString = new JSONObject()
                                .put("oldPassword", psw)
                                .put("newPassword", newPsw)
                                .put("newPasswordAgain", newPswAgain)
                                .toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    login.edit().putString("pwd", newPsw).apply();
                    RequestTask pswChange = new RequestTask(getContext(),"change-password", "PATCH", pswChangeString);
                    pswChange.execute();
                    dismiss();
                }
            }
        });
        passwordChangeDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        meter.setPasswordStrengthCalculator(new PasswordStrengthCalculator() {
            @Override
            public int calculatePasswordSecurityLevel(String password) {
                if (password.length() > 10 && PswCheck.pwdDigitCheck(password) &&
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
                return level > 2;
            }

            @Override
            public void onPasswordAccepted(String password) {
                pwdIsValid = true;
            }
        });
        return builder;
    }

    private void init(View view){
        passwordChangeDialogPassword = view.findViewById(R.id.password_change_dialog_password);
        passwordChangeDialogNewPassword = view.findViewById(R.id.password_change_dialog_new_password);
        passwordChangeDialogNewPasswordAgain = view.findViewById(R.id.password_change_dialog_new_password_again);
        passwordChangeDialogPasswordError = view.findViewById(R.id.password_change_dialog_password_error);
        passwordChangeDialogNewPasswordError = view.findViewById(R.id.password_change_dialog_new_password_error);
        passwordChangeDialogNewPasswordAgainError = view.findViewById(R.id.password_change_dialog_new_password_again_error);
        passwordChangeDialogEditBtn = view.findViewById(R.id.password_change_dialog_edit_btn);
        passwordChangeDialogCancelBtn = view.findViewById(R.id.password_change_dialog_cancel_btn);

        meter = view.findViewById(R.id.password_change_dialog_pwd_meter);
        meter.setEditText(passwordChangeDialogNewPassword);

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

    private void errorReset() {
        passwordChangeDialogPasswordError.setText("");
        passwordChangeDialogNewPasswordError.setText("");
        passwordChangeDialogNewPasswordAgainError.setText("");
    }
}
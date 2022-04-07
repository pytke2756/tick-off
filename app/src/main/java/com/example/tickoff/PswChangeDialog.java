package com.example.tickoff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.tickoff.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class PswChangeDialog extends AppCompatDialogFragment{
    private TextInputEditText passwordChangeDialogPassword;
    private TextInputEditText passwordChangeDialogNewPassword;
    private TextInputEditText passwordChangeDialogNewPasswordAgain;
    private AppCompatTextView passwordChangeDialogPasswordError;
    private AppCompatTextView passwordChangeDialogNewPasswordError;
    private AppCompatTextView passwordChangeDialogNewPasswordAgainError;
    private MaterialButton passwordChangeDialogEditBtn;
    private MaterialButton passwordChangeDialogCancelBtn;

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
                if (isValid){
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
                    RequestTask pswChange = new RequestTask(getContext(),"http://10.0.2.2:5000/change-password", "PATCH", pswChangeString);
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
    }

    private void errorReset() {
        passwordChangeDialogPasswordError.setText("");
        passwordChangeDialogNewPasswordError.setText("");
        passwordChangeDialogNewPasswordAgainError.setText("");
    }
}
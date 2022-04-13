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


public class UsernameChangeDialog extends AppCompatDialogFragment{
    private TextInputEditText usernameChangeDialogUsername;
    private TextInputEditText usernameChangeDialogPassword;
    private AppCompatTextView usernameChangeDialogPasswordError;
    private AppCompatTextView usernameChangeDialogUsernameError;
    private MaterialButton usernameChangeDialogEditBtn;
    private MaterialButton usernameChangeDialogCancelBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.username_change_dialog,null);
        builder.setView(view);
        init(view);
        usernameChangeDialogEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorReset();
                String newUsername = usernameChangeDialogUsername.getText().toString().trim();
                String psw = usernameChangeDialogPassword.getText().toString().trim();
                boolean isValid = true;
                SharedPreferences login = getContext().getSharedPreferences("TickOff", Context.MODE_PRIVATE);
                if (newUsername.isEmpty()){
                    usernameChangeDialogUsernameError.setText(R.string.empty_input_error);
                    isValid = false;
                }
                if (psw.isEmpty()){
                    usernameChangeDialogPasswordError.setText(R.string.empty_input_error);
                    isValid = false;
                }
                if (!psw.equals(login.getString("pwd", ""))){
                    usernameChangeDialogPasswordError.setText("Hibás jelszó");
                    isValid = false;
                }
                if (isValid){
                    String usernameChangeString = "";
                    try {
                        usernameChangeString = new JSONObject()
                                .put("password", psw)
                                .put("newUsername", newUsername)
                                .toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestTask pswChange = new RequestTask(getContext(),"change-username", "PATCH", usernameChangeString);
                    pswChange.execute();
                    dismiss();
                }
            }
        });
        usernameChangeDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder;
    }

    private void init(View view){
        usernameChangeDialogUsername = view.findViewById(R.id.username_change_dialog_username);
        usernameChangeDialogPassword = view.findViewById(R.id.username_change_dialog_password);
        usernameChangeDialogPasswordError = view.findViewById(R.id.username_change_dialog_password_error);
        usernameChangeDialogUsernameError = view.findViewById(R.id.username_change_dialog_username_error);
        usernameChangeDialogEditBtn = view.findViewById(R.id.username_change_dialog_edit_btn);
        usernameChangeDialogCancelBtn = view.findViewById(R.id.username_change_dialog_cancel_btn);
    }

    private void errorReset() {
        usernameChangeDialogPasswordError.setText("");
        usernameChangeDialogUsernameError.setText("");
    }
}


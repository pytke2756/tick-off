package com.example.tickoff;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class DataChangeDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText dataChangeDialogLastname;
    private TextInputEditText dataChangeDialogFirstname;
    private AppCompatTextView dataChangeDialogLastnameError;
    private AppCompatTextView dataChangeDialogFirstnameError;
    private AppCompatTextView dataChangeDialogDateError;
    private TextInputEditText dataChangeDialogDate;
    private MaterialButton dataChangeDialogEditBtn;
    private MaterialButton dataChangeDialogCancelBtn;

    private long dateInteger;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.data_change_dialog,null);
        init(view);
        builder.setView(view);
        dataChangeDialogEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorReset();
                String firstname = dataChangeDialogFirstname.getText().toString().trim();
                String lastname = dataChangeDialogLastname.getText().toString().trim();
                String ido = dataChangeDialogDate.getText().toString().trim();

                String userDataChangeString = "";
                try {
                    userDataChangeString = new JSONObject()
                            .put("newFirstName", firstname)
                            .put("newLastName", lastname)
                            .put("newBornDate", dateInteger)
                            .toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestTask pswChange = new RequestTask(getContext(),"change-data", "PATCH", userDataChangeString);
                pswChange.execute();
                dismiss();
            }
        });
        dataChangeDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder;
    }

    private void init(View view){
        dataChangeDialogLastname = view.findViewById(R.id.data_change_dialog_lastname);
        dataChangeDialogFirstname = view.findViewById(R.id.data_change_dialog_firstname);
        dataChangeDialogDate = view.findViewById(R.id.data_change_dialog_date);
        dataChangeDialogLastnameError = view.findViewById(R.id.data_change_dialog_lastname_error);
        dataChangeDialogFirstnameError = view.findViewById(R.id.data_change_dialog_firstname_error);
        dataChangeDialogDateError = view.findViewById(R.id.data_change_dialog_date_error);
        dataChangeDialogEditBtn = view.findViewById(R.id.data_change_dialog_edit_btn);
        dataChangeDialogCancelBtn = view.findViewById(R.id.data_change_dialog_cancel_btn);

        dateInteger = 0;

        dataChangeDialogDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBirthPickerDialog();
            }
        });
    }

    private void errorReset() {
        dataChangeDialogLastnameError.setText("");
        dataChangeDialogFirstnameError.setText("");
        dataChangeDialogDateError.setText("");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String monthString = String.valueOf(month+1);
        String dayString = String.valueOf(dayOfMonth);
        if (month < 10){
            monthString = "0"+(month+1);
        }
        if (dayOfMonth < 10){
            dayString = "0" + dayString;
        }
        String date = year + "." + monthString  + "." + dayString + ".";
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, dayOfMonth);
        c.set(Calendar.YEAR, year);
        Date d = c.getTime();
        dateInteger = UnixDateConverter.toUnixTime(d.getTime());
        dataChangeDialogDate.setText(date);
    }

    private void newBirthPickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.datePickerTheme,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}


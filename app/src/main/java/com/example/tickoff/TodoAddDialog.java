package com.example.tickoff;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;

public class TodoAddDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
    private EditText editTextTitle;
    private EditText editTextCategory;
    private EditText editTextDate;
    private TodoAddDialogListener todoAddDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.todo_add_dialog,null);

        builder.setView(view)
                .setTitle("Todo hozzáadása")
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Hozzáad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = editTextTitle.getText().toString();
                        String category = editTextCategory.getText().toString();
                        String date = editTextDate.getText().toString();
                        todoAddDialogListener.dataSend(title, category, date);
                    }
                });
        init(view);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            todoAddDialogListener = (TodoAddDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        editTextDate.setText(date);
    }

    public interface TodoAddDialogListener{
        void dataSend(String title, String category, String date);
    }

    private void init(View view){
        editTextTitle = view.findViewById(R.id.todo_dialog_title);
        editTextCategory = view.findViewById(R.id.todo_dialog_category);
        editTextDate = view.findViewById(R.id.todo_dialog_date);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthPickerDialog();
            }
        });
    }

    public void setListener(TodoAddDialogListener listener){
        this.todoAddDialogListener = listener;
    }

    private void birthPickerDialog(){
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

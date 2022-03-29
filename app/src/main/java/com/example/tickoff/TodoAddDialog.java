package com.example.tickoff;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TodoAddDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText editTextTitle;
    private AppCompatSpinner todoDialogCategorySpinner;
    private TextInputEditText editTextDate;
    private TodoAddDialogListener todoAddDialogListener;
    private AppCompatTextView todoDialogTitleError;
    private AppCompatTextView todoDialogCategoryError;
    private AppCompatTextView todoDialogDateError;

    private long dateInteger;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.todo_add_dialog,null);
        String[] catgories = new String[]{"Tanulás", "Munka", "Bevásárlás", "Család", "Számlák"};
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
                        String title = editTextTitle.getText().toString().trim();
                        String category = todoDialogCategorySpinner.getSelectedItem().toString().trim();
                        String date = editTextDate.getText().toString();
                        boolean isValid = true;
                        if (title.isEmpty()){
                            todoDialogTitleError.setText(R.string.empty_input_error);
                            isValid = false;
                        }
                        if (category.isEmpty()){
                            todoDialogCategoryError.setText(R.string.empty_input_error);
                            isValid = false;
                        }
                        if (date.isEmpty()){
                            todoDialogDateError.setText(R.string.empty_input_error);
                            isValid = false;
                        }
                        if (isValid){
                            todoAddDialogListener.dataSend(title, Categories.getIndex(category), dateInteger);
                            HashMap<String, String> todoAddMap = new HashMap<String, String>();
                            todoAddMap.put("todo", title);
                            todoAddMap.put("categoryID", String.valueOf(Categories.getIndex(category)));
                            JSONObject todoAddJSON = new JSONObject(todoAddMap);
                            Log.d("JSON ADD", todoAddJSON.toString());
                            RequestTask todoAdd = new RequestTask(getContext(), "http://localhost:5000/todo", "POST", todoAddJSON.toString());
                            todoAdd.execute();
                        }
                        else{
                            Toast.makeText(getContext(), "Kötelező mindent kitölteni", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        init(view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, catgories);
        todoDialogCategorySpinner.setAdapter(adapter);

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
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 3);
        c.set(Calendar.DATE, 29);
        c.set(Calendar.YEAR, 2022);
        Date d = c.getTime();
        dateInteger = d.getTime();
        editTextDate.setText(date);
    }

    public interface TodoAddDialogListener{
        void dataSend(String title, int category, long date);
    }

    private void init(View view){
        editTextTitle = view.findViewById(R.id.todo_dialog_title);
        todoDialogCategorySpinner = view.findViewById(R.id.todo_dialog_category_spinner);
        editTextDate = view.findViewById(R.id.todo_dialog_date);
        todoDialogCategoryError = view.findViewById(R.id.todo_dialog_category_error);
        todoDialogTitleError = view.findViewById(R.id.todo_dialog_title_error);
        todoDialogDateError = view.findViewById(R.id.todo_dialog_date_error);



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

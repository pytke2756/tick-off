package com.example.tickoff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditTodoDialog extends AppCompatDialogFragment {
    private TextInputEditText editTodoDialogTitle;
    private AppCompatTextView editTodoDialogTitleError;
    private AppCompatSpinner editTodoDialogCategorySpinner;
    private AppCompatTextView editTodoDialogCategoryError;
    private TextInputEditText editTodoDialogDate;
    private AppCompatTextView editTodoDialogDateError;
    private MaterialButton editTodoDialogEditBtn;
    private MaterialButton editTodoDialogCancelBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_todo_dialog,null);
        builder.setView(view);
        init(view);

        editTodoDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        editTodoDialogEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorReset();

                /*String todoEditString = "";
                        try {
                            todoEditString = new JSONObject()
                                    .put("id", todo.getId())
                                    .put("done", 1)
                                    .put("todoText", todo.getTodo())
                                    .put("categoryID", todo.getCategory_id())
                                    .put("important", todo.isImportant())
                                    .toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestTask patch = new RequestTask(view.getContext(), "todo", "PATCH", todoEditString);
                        patch.execute();*/

                dismiss();
            }
        });

        return builder;
    }

    private void init(View view) {
        editTodoDialogTitle = view.findViewById(R.id.edit_todo_dialog_title);
        editTodoDialogTitleError = view.findViewById(R.id.edit_todo_dialog_title_error);
        editTodoDialogCategorySpinner = view.findViewById(R.id.edit_todo_dialog_category_spinner);
        editTodoDialogCategoryError = view.findViewById(R.id.edit_todo_dialog_category_error);
        editTodoDialogDate = view.findViewById(R.id.edit_todo_dialog_date);
        editTodoDialogDateError = view.findViewById(R.id.edit_todo_dialog_date_error);
        editTodoDialogEditBtn = view.findViewById(R.id.edit_todo_dialog_edit_btn);
        editTodoDialogCancelBtn = view.findViewById(R.id.edit_todo_dialog_cancel_btn);
    }

    private void errorReset() {
        editTodoDialogTitleError.setText("");
        editTodoDialogCategoryError.setText("");
        editTodoDialogDateError.setText("");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}

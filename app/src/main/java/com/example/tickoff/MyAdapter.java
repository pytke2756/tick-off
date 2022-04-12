package com.example.tickoff;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tickoff.activities.MainActivity;
import com.google.android.material.button.MaterialButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
    Context context;
    List<Todo> todos;
    View view;

    public MyAdapter(Context context, List<Todo> todos) {
        this.context = context;
        this.todos = todos;
    }

    @NonNull
    @Override
    public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_todo,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.todoTitle.setText(todos.get(position).getTodo());
        holder.todoEndDate.setText(UnixDateConverter.toDateString(todos.get(position).getDeadline()));
        holder.todoCategory.setText(Categories.getCategory(todos.get(position).getCategory_id()));
        holder.todo = todos.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.edit_todo_window, null);

                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                MaterialButton edit = popupView.findViewById(R.id.edit_todo_window_todo_edit);
                MaterialButton delete = popupView.findViewById(R.id.edit_todo_window_todo_delete);
                MaterialButton done = popupView.findViewById(R.id.edit_todo_window_todo_done);

                AppCompatTextView title = popupView.findViewById(R.id.edit_todo_window_todo_title);
                AppCompatTextView category = popupView.findViewById(R.id.edit_todo_window_todo_category);
                AppCompatTextView date = popupView.findViewById(R.id.edit_todo_window_todo_date);

                title.setText(todos.get(position).getTodo());
                category.setText(Categories.getCategory(todos.get(position).getCategory_id()));
                date.setText(UnixDateConverter.toDateString(todos.get(position).getDeadline()));


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String todoDeleteString = "";
                        try {
                            todoDeleteString = new JSONObject()
                                    .put("id", todos.get(position).getId())
                                    .toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestTask delete = new RequestTask(view.getContext(), "todo", "DELETE", todoDeleteString);
                        delete.execute();
                        popupWindow.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle data = new Bundle();
                        data.putString("title", todos.get(position).getTodo());
                        data.putInt("category", todos.get(position).getCategory_id());
                        data.putLong("date", todos.get(position).getDeadline());

                        EditTodoDialog editTodo=new EditTodoDialog();
                        Toast.makeText(view.getContext(), "Ez a funkció nem elérhető", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String todoDoneString = "";
                        try {
                            todoDoneString = new JSONObject()
                                    .put("id", todos.get(position).getId())
                                    .put("done", 1)
                                    .put("todoText", todos.get(position).getTodo())
                                    .put("categoryID", todos.get(position).getCategory_id())
                                    .put("important", todos.get(position).isImportant())
                                    .toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("TODO DONE", todoDoneString);
                        RequestTask patch = new RequestTask(view.getContext(), "todo", "PATCH", todoDoneString);
                        patch.execute();
                        popupWindow.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView todoTitle;
        TextView todoEndDate;
        TextView todoCategory;

        ImageButton todoDone;
        ImageButton todoCancel;

        Todo todo;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            todoTitle = itemView.findViewById(R.id.todo_title);
            todoEndDate = itemView.findViewById(R.id.todo_end_date);
            todoCategory = itemView.findViewById(R.id.todo_category);

            todoDone = itemView.findViewById(R.id.todo_done);
            todoCancel = itemView.findViewById(R.id.todo_cancel);

            todoDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = todo.getTodo() + " kész";
                    String todoDoneString = "";
                    try {
                        todoDoneString = new JSONObject()
                                .put("id", todo.getId())
                                .put("done", 1)
                                .put("todoText", todo.getTodo())
                                .put("categoryID", todo.getCategory_id())
                                .put("important", todo.isImportant())
                                .toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("TODO DONE", todoDoneString);
                    RequestTask patch = new RequestTask(view.getContext(), "todo", "PATCH", todoDoneString);
                    patch.execute();
                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });

            todoCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String todoDeleteString = "";

                    try {
                        todoDeleteString = new JSONObject()
                                .put("id", todo.getId())
                                .toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestTask delete = new RequestTask(view.getContext(), "todo", "DELETE", todoDeleteString);
                    delete.execute();

                    String message = todo.getTodo() + " törölve";
                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

package com.example.tickoff;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
    Context context;
    List<Todo> todos;

    public MyAdapter(Context context, List<Todo> todos) {
        this.context = context;
        this.todos = todos;
    }

    @NonNull
    @Override
    public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_todo,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {
        holder.todoTitle.setText(todos.get(position).getTodo());
        holder.todoEndDate.setText(UnixDateConverter.toDateString(todos.get(position).getDeadline())); //TODO: ezt javítani mert ez most teszt, kell az endDate
        holder.todoCategory.setText(Categories.getCategory(todos.get(position).getCategory_id()));
        holder.todo = todos.get(position);
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
                    RequestTask patch = new RequestTask(view.getContext(), "http://10.0.2.2:5000/todo", "PATCH", todoDoneString);
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
                    RequestTask delete = new RequestTask(view.getContext(), "http://10.0.2.2:5000/todo", "DELETE", todoDeleteString);
                    delete.execute();

                    String message = todo.getTodo() + " törölve";
                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

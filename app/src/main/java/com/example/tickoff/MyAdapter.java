package com.example.tickoff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
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
        holder.todoTitle.setText(todos.get(position).getTodoTitle());
        holder.todoEndDate.setText(todos.get(position).getTodoEndDate());
        holder.todoCategory.setText(todos.get(position).getTodoCategory());
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView todoTitle;
        TextView todoEndDate;
        TextView todoCategory;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            todoTitle = itemView.findViewById(R.id.todo_title);
            todoEndDate = itemView.findViewById(R.id.todo_end_date);
            todoCategory = itemView.findViewById(R.id.todo_category);
        }
    }
}

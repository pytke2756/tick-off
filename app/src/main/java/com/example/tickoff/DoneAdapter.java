package com.example.tickoff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.DoneHolder>{
    Context context;
    List<Todo> doneTodos;
    View view;


    public DoneAdapter(Context context, List<Todo> doneTodos) {
        this.context = context;
        this.doneTodos = doneTodos;
    }

    @NonNull
    @Override
    public DoneAdapter.DoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_done_todo,parent,false);
        return new DoneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneAdapter.DoneHolder holder, int position) {
        holder.todoDoneTitle.setText(doneTodos.get(position).getTodo());
        holder.todoDoneCategory.setText(Categories.getCategory(doneTodos.get(position).getCategory_id()));
        holder.todoDoneCreated.setText("Létrehozva: " + UnixDateConverter.toDateString(doneTodos.get(position).getCreation_date()));
        holder.todoDoneEnd.setText("Befejezve: " + UnixDateConverter.toDateString(doneTodos.get(position).getEnd_date()));
    }

    @Override
    public int getItemCount() {
        return doneTodos.size();
    }

    public static class DoneHolder extends RecyclerView.ViewHolder{

        AppCompatTextView todoDoneTitle;
        AppCompatTextView todoDoneCategory;
        AppCompatTextView todoDoneCreated;
        AppCompatTextView todoDoneEnd;

        public DoneHolder(@NonNull View itemView) {
            super(itemView);

            todoDoneTitle = itemView.findViewById(R.id.todo_done_title);
            todoDoneCategory = itemView.findViewById(R.id.todo_done_category);
            todoDoneCreated = itemView.findViewById(R.id.todo_done_created);
            todoDoneEnd = itemView.findViewById(R.id.todo_done_end);

        }
    }
}

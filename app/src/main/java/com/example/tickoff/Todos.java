package com.example.tickoff;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Todos extends Fragment implements TodoAddDialog.TodoAddDialogListener {

    private FloatingActionButton todoAdd;
    private RecyclerView todosUserTodos;
    private MyAdapter myAdapter;

    List<Todo> todos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todos, container, false);

        init(view);

        todosUserTodos.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        todosUserTodos.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(getContext(), todos);
        todosUserTodos.setAdapter(myAdapter);

        todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoAdd();
            }
        });

        return view;
    }

    private void init(View view){
        todoAdd = view.findViewById(R.id.add_todo);
        todosUserTodos = view.findViewById(R.id.todos_user_todos);
        todos = new ArrayList<>();
        todos.add(new Todo("Boltba menni", "2022.03.03", "Bevásárlás"));
        todos.add(new Todo("Takarítani", "2022.04.19", "Takarítás"));
        todos.add(new Todo("Vizsgamunka", "2022.04.20", "Tanulás"));
    }

    private void todoAdd(){
        TodoAddDialog todoAddDialog = new TodoAddDialog();
        todoAddDialog.show(getActivity().getSupportFragmentManager(), "teszt");
        todoAddDialog.setListener(this);
    }

    @Override
    public void dataSend(String title, String category, String date) {
        todos.add(new Todo(title, date, category));
        todosUserTodos.setAdapter(myAdapter);
    }
}
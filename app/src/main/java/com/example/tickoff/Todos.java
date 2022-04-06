package com.example.tickoff;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Todos extends Fragment implements TodoAddDialog.TodoAddDialogListener, DataToFragments{

    private FloatingActionButton todoAdd;
    private RecyclerView todosUserTodos;
    private MyAdapter myAdapter;

    public List<Todo> todos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setdata(this);
        }

        View view = inflater.inflate(R.layout.fragment_todos, container, false);

        init(view);

        todosUserTodos.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        todosUserTodos.setLayoutManager(layoutManager);


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
    }

    private void todoAdd(){
        TodoAddDialog todoAddDialog = new TodoAddDialog();
        todoAddDialog.show(getActivity().getSupportFragmentManager(), "teszt");
        todoAddDialog.setListener(this);
    }

    @Override
    public void dataSend(String title, int category, long date) {
        String todoAddString = "";
        try {
            todoAddString = new JSONObject()
                    .put("todo", title)
                    .put("important", 0)
                    .put("deadline", date)
                    .put("categoryID", category)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestTask todoAdd = new RequestTask(getContext(), "http://10.0.2.2:5000/todo", "POST",todoAddString);
        todoAdd.execute();
    }

    private boolean isTodoInList(JSONObject obj){
        for (int i = 0; i < todos.size(); i++) {
            try {
                if (todos.get(i).getId() == obj.getInt("id")){
                    Gson gson= new Gson();
                    Todo t = gson.fromJson(obj.toString(),Todo.class);
                    todos.set(i,t);
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void adapterSet(){
        myAdapter = new MyAdapter(getActivity(), todos);
        todosUserTodos.setAdapter(myAdapter);
    }

    @Override
    public void sendData(Response response) {
        JSONObject res = null;
        JSONArray data;
        Object todoArrayOrNot;
        Type type = new TypeToken<List<Todo>>(){}.getType();
        Gson obj = new Gson();
        try {
            res = new JSONObject(response.getContent());
            todoArrayOrNot = res.get("data");
            if (todoArrayOrNot instanceof String){
                if (todoArrayOrNot.equals("todo successfully deleted")){
                    RequestTask refresh = new RequestTask(getContext(), "http://10.0.2.2:5000/todo", "GET");
                    refresh.execute();
                }
            }

            if (todoArrayOrNot instanceof JSONArray){
                data = new JSONArray(todoArrayOrNot.toString());
                if (data.length() >= 1){
                    todos = obj.fromJson(data.toString(), type);
                    adapterSet();
                }
            }
            else if (todoArrayOrNot instanceof JSONObject){
                JSONObject todo = new JSONObject(todoArrayOrNot.toString());
                if (isTodoInList(todo)){
                    adapterSet();
                }
                else {
                    todos.add(new Todo(todo.getInt("id"), todo.getInt("user_id"),
                            todo.getString("todo"), todo.getInt("category_id"),
                            todo.getLong("creation_date"), todo.getLong("deadline"),
                            todo.getLong("end_date"),
                            todo.getBoolean("done"), todo.getBoolean("important")));
                    adapterSet();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
package com.example.tickoff;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.List;

public class Todos extends Fragment implements TodoAddDialog.TodoAddDialogListener, DataToFragments{

    private FloatingActionButton todoAdd;
    private RecyclerView todosUserTodos;
    private MyAdapter myAdapter;

    List<Todo> todos;

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
        todos.add(new Todo(title, category, date));
        todosUserTodos.setAdapter(myAdapter);
    }

    @Override
    public void sendData(Response response) {
        JSONObject res;
        JSONArray data = null;
        try {
            res = new JSONObject(response.getContent());
            data = new JSONArray(res.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (data != null){
            Gson obj = new Gson();
            Type type = new TypeToken<List<Todo>>(){}.getType();
            todos = obj.fromJson(data.toString(), type);
        }
        myAdapter = new MyAdapter(getActivity(), todos);
        todosUserTodos.setAdapter(myAdapter);
    }
}
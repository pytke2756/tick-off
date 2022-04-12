package com.example.tickoff.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tickoff.DataToFragments;
import com.example.tickoff.DoneAdapter;
import com.example.tickoff.MyAdapter;
import com.example.tickoff.R;
import com.example.tickoff.Response;
import com.example.tickoff.Todo;
import com.example.tickoff.activities.MainActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Done extends Fragment implements DataToFragments {
    private RecyclerView todosUserDoneTodos;
    private DoneAdapter doneAdapter;

    public List<Todo> doneTodos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setdata(this);
        }

        View view = inflater.inflate(R.layout.fragment_done, container, false);

        init(view);

        todosUserDoneTodos.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        todosUserDoneTodos.setLayoutManager(layoutManager);


        return view;
    }

    private void init(View view) {
        todosUserDoneTodos = view.findViewById(R.id.todos_user_done_todos);
        doneTodos = new ArrayList<>();
    }

    private void adapterSet(){
        doneAdapter = new DoneAdapter(getActivity(), doneTodos);
        todosUserDoneTodos.setAdapter(doneAdapter);
    }

    @Override
    public void sendData(Response response) {
        Object arrayOrNot;
        JSONArray dataArray;
        Gson obj = new Gson();
        try {
            JSONObject res = new JSONObject(response.getContent());
            arrayOrNot = res.get("data");

            if (arrayOrNot instanceof JSONArray){
                dataArray = new JSONArray(arrayOrNot.toString());
                if (dataArray.length() > 0){
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject todo = dataArray.getJSONObject(i);
                        if (todo.getBoolean("done")){
                            Todo t = obj.fromJson(todo.toString(), Todo.class);
                            doneTodos.add(t);
                        }
                    }
                    adapterSet();
                }
                else if (dataArray.length() == 0){
                    doneTodos = new ArrayList<>();
                    adapterSet();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

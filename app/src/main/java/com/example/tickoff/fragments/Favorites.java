package com.example.tickoff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tickoff.DataToFragments;
import com.example.tickoff.DoneAdapter;
import com.example.tickoff.MyAdapter;
import com.example.tickoff.R;
import com.example.tickoff.RequestTask;
import com.example.tickoff.Response;
import com.example.tickoff.Todo;
import com.example.tickoff.activities.MainActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends Fragment implements DataToFragments {
    private RecyclerView todosUserFavoritesTodos;
    private MyAdapter myAdapter;

    public List<Todo> favoritesTodos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setdata(this);
        }

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        init(view);

        todosUserFavoritesTodos.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        todosUserFavoritesTodos.setLayoutManager(layoutManager);
        return view;
    }

    private void init(View view) {
        todosUserFavoritesTodos = view.findViewById(R.id.todos_user_favorites_todos);
        favoritesTodos = new ArrayList<>();
    }

    private void adapterSet(){
        myAdapter = new MyAdapter(getActivity(), favoritesTodos);
        todosUserFavoritesTodos.setAdapter(myAdapter);
    }


    @Override
    public void sendData(Response response) {
        Object arrayOrNot;
        Gson obj = new Gson();
        JSONArray data;
        JSONObject res = null;
        try{
            res = new JSONObject(response.getContent());
            arrayOrNot = res.get("data");
            if (arrayOrNot instanceof String){
                if (arrayOrNot.equals("todo successfully deleted")){
                    RequestTask refresh = new RequestTask(getContext(), "todo", "GET");
                    refresh.execute();
                    adapterSet();
                }
            }
            else if (arrayOrNot instanceof JSONArray){
                data = new JSONArray(arrayOrNot.toString());
                favoritesTodos.clear();
                Log.d("Favorites", data.toString());
                if (data.length() > 0){
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject todo = data.getJSONObject(i);
                        if (!todo.getBoolean("done") && todo.getBoolean("important")){
                            Todo t = obj.fromJson(todo.toString(), Todo.class);
                            favoritesTodos.add(t);
                        }
                    }
                    adapterSet();
                }
                else if (data.length() == 0){
                    favoritesTodos = new ArrayList<>();
                    adapterSet();
                    Toast.makeText(getContext(), "Nincsen fontos teendőd", Toast.LENGTH_SHORT).show();
                }
            }
            else if(arrayOrNot instanceof JSONObject){
                JSONObject todo = (JSONObject) arrayOrNot;
                if (!todo.getBoolean("important")){
                    for (int i = 0; i < favoritesTodos.size(); i++) {
                        if (favoritesTodos.get(i).getId() == todo.getInt("id")){
                            favoritesTodos.remove(i);
                        }
                    }
                }
                if (todo.getBoolean("done")){
                    for (int i = 0; i < favoritesTodos.size(); i++) {
                        if (favoritesTodos.get(i).getId() == todo.getInt("id")){
                            favoritesTodos.remove(i);
                        }
                    }
                }
                adapterSet();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
package com.example.tickoff;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Todos extends Fragment {

    private FloatingActionButton todoAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todos, container, false);

        init(view);

        todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: a gombra nyomva felugrik a felvétel ablak
                Toast.makeText(getContext(),"Hozzáad", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void init(View view){
        todoAdd = view.findViewById(R.id.add_todo);
    }
}
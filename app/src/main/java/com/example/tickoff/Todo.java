package com.example.tickoff;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo {
    private int id;
    private int user_id;
    private String todo;
    private int category_id;
    private long creation_date;
    private boolean done;

    public Todo(int id, int user_id, String todo, int category_id, int creation_date, boolean done) {
        this.id = id;
        this.user_id = user_id;
        this.todo = todo;
        this.category_id = category_id;
        this.creation_date = creation_date;
        this.done = done;
    }

    public Todo(String todo, int category_id, long creation_date) {
        this.todo = todo;
        this.category_id = category_id;
        this.creation_date = creation_date;
        this.done = false;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTodo() {
        return todo;
    }

    public int getCategory_id() {
        return category_id;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public boolean getDone() {
        return done;
    }
}

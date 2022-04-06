package com.example.tickoff;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo {
    private int id;
    private int user_id;
    private String todo;
    private int category_id;
    private long creation_date;
    private long deadline;
    private long end_date;
    private boolean done;
    private boolean important;


    public Todo(int id, int user_id, String todo, int category_id, long creation_date, long deadline,
                long end_date, boolean done, boolean important) {
        this.id = id;
        this.user_id = user_id;
        this.todo = todo;
        this.category_id = category_id;
        this.creation_date = creation_date;
        this.deadline = deadline;
        this.end_date = end_date;
        this.deadline = deadline;
        this.done = done;
        this.important = important;
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

    public long getEnd_date() {
        return end_date;
    }

    public boolean isDone() {
        return done;
    }

    public long getDeadline() {
        return deadline;
    }

    public boolean isImportant() {
        return important;
    }
}

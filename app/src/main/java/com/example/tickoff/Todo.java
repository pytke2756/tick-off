package com.example.tickoff;

public class Todo {
    private String todoTitle;
    private String todoEndDate;
    private String todoCategory;

    public Todo(String todoTitle, String todoEndDate, String todoCategory) {
        this.todoTitle = todoTitle;
        this.todoEndDate = todoEndDate;
        this.todoCategory = todoCategory;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public String getTodoEndDate() {
        return todoEndDate;
    }

    public String getTodoCategory() {
        return todoCategory;
    }
}

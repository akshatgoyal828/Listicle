package com.example.trackandtrigger;


public class Entry {
    private String title;
    private String description;
    private int priority;

    public Entry() {
        //empty constructor needed
    }

    public Entry(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
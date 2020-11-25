package com.example.trackandtrigger;

/* For toDoFragment */

public class CollectionItem {
    private String title;
    private int priority;

    public CollectionItem() {
        //empty constructor needed
    }

    public CollectionItem(String title, int priority) {
        this.title = title;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }
    public int getPriority() {
        return priority;
    }
}
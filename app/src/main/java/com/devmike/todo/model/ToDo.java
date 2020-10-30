package com.devmike.todo.model;

import java.util.UUID;

public class ToDo {

    private String id;
    private String title;
    private String description;

    public ToDo() {
    }

    public ToDo(String id, String title, String description) {
        if (id != null) {
            this.id = id;
        } else {
            this.id = UUID.randomUUID().toString();
        }
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title;
    }
}

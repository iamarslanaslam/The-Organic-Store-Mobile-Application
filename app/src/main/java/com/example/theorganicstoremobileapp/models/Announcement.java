package com.example.theorganicstoremobileapp.models;

import java.io.Serializable;

public class Announcement implements Serializable {
    private String id;
    private String title;
    private String date;
    private String type;
    private String description;

    public Announcement() {
        // Default constructor required for calls to DataSnapshot.getValue(Announcement.class)
    }

    public Announcement(String id, String title, String date, String type, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

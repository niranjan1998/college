package com.example.college;

public class dash_notes_model {
    public String name;
    public String url;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public dash_notes_model() {
    }

    public dash_notes_model(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
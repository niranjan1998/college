package com.example.college;

public class uploads_fmodel {
    public String name;
    public String url;
    private String key;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public uploads_fmodel() {
    }

    public uploads_fmodel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

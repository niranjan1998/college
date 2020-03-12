package com.example.college;

public class event_model {
    public event_model() {
    }

    private String event_name;
    private String event_dep;
    private String event_img;
    private String event_date;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public event_model(String event_name, String event_dep, String event_img, String event_date) {
        this.event_name = event_name;
        this.event_dep = event_dep;
        this.event_img = event_img;
        this.event_date = event_date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_dep() {
        return event_dep;
    }

    public void setEvent_dep(String event_dep) {
        this.event_dep = event_dep;
    }


    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public void setEvent_img(String event_img) {
        this.event_img = event_img;
    }

    public String getEvent_img() {
        return event_img;
    }
}

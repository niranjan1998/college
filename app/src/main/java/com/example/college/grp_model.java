package com.example.college;

public class grp_model {
    private String user_name;
    private String user_msg;
    private String user_key;

    public grp_model(String user_name, String user_msg, String user_key) {
        this.user_name = user_name;
        this.user_msg = user_msg;
        this.user_key = user_key;
    }

    public grp_model() {
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }
}

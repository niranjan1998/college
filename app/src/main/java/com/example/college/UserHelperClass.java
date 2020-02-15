package com.example.college;

public class UserHelperClass {

    String name,roll,phone,email,password,stream;

    public UserHelperClass(){}

    public UserHelperClass(String name, String roll, String phone, String email, String password,String stream) {
        this.name = name;
        this.roll = roll;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.stream = stream;

    }



    public String getName() {
        return name;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

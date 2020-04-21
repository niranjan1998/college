package project.msc.college;

import android.content.Context;
import android.content.SharedPreferences;

public class storeUser {
//shared preference errors
    Context context;

    private String pass;
    private  String name;
    SharedPreferences sharedPreferences;

    public String getPass() {
       pass = sharedPreferences.getString("pass","");
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
        sharedPreferences.edit().putString("pass",pass).commit();
    }


    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }

    public String getName() {
        name = sharedPreferences.getString("name","");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("name",name).commit();
    }



    public storeUser(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

    }
}

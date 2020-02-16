package com.example.college;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (new storeUser(home.this).getName() != "") {

                    Intent intent = new Intent(home.this, userLogin.class);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(home.this, dashboard.class);
                    startActivity(intent);
                }
            }
        }, 2000);
    }

    public void signupBtn(View view) {

        Intent intent = new Intent(this, RegUsers.class);
        startActivity(intent);
    }

    public void loginBtn(View view) {

        Intent intent = new Intent(home.this, userLogin.class);
        startActivity(intent);

    }

    public void dashBtn(View view) {

        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }

}

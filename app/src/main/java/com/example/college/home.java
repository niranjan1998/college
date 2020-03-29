package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class home extends AppCompatActivity {
    Animation name_ani;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name_ani = AnimationUtils.loadAnimation(this,R.anim.name_animation);
        name = findViewById(R.id.college_app);
        name.setAnimation(name_ani);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                    Intent intent = new Intent(home.this, userLogin.class);
                    startActivity(intent);
                    finish();

            }
        }, 1000);
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

package com.example.college;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void signupBtn(View view) {

        Intent intent = new Intent(this, RegUsers.class);
        startActivity(intent);
    }

    public void loginBtn(View view) {

        Intent intent = new Intent(this, userLogin.class);
        startActivity(intent);
    }

    public void dashBtn(View view) {

        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }

}
package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class admin_panel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }

    public void add_teacher(View view) {
        Intent intent = new Intent(this, reg_teacher.class);
        startActivity(intent);
    }

    public void add_student(View view) {
        Intent intent = new Intent(this, RegUsers.class);
        startActivity(intent);
    }

    public void show_students(View view) {
        Intent intent = new Intent(this, admin_show_std.class);
        startActivity(intent);
    }

    public void show_teacher(View view) {
        Intent intent = new Intent(this, admin_show_std.class);
        startActivity(intent);
    }
}

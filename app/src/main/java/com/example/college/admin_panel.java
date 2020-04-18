package com.example.college;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        Intent intent = new Intent(getApplicationContext(), admin_show_std.class);
        intent.putExtra("role","Student");
        startActivity(intent);
    }

    public void show_teacher(View view) {
        Intent teacher = new Intent(getApplicationContext(), admin_show_std.class);
        teacher.putExtra("role", "Teacher");
        startActivity(teacher);
    }

    public void logout(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("LOGOUT")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //removing values from sp
                        getApplicationContext().getSharedPreferences("loginUser", 0).edit().clear().apply();

                        Toast.makeText(getApplicationContext(), "Logout Done", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(admin_panel.this, home.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        /*
        //removing values from sp
        getApplicationContext().getSharedPreferences("loginUser", 0).edit().clear().apply();

        Toast.makeText(getApplicationContext(), "Logout Done", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(admin_panel.this, home.class);
        startActivity(intent);
        finish();*/

    }
}

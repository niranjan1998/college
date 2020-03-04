package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class dashboard extends AppCompatActivity {

    TextView txtName, txtFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtName = findViewById(R.id.txtUsername);
        txtFields = findViewById(R.id.txtField);

        showAllUserData();

    }

    public void showAllUserData() {

        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        String stream = intent.getStringExtra("stream");

        txtName.setText(userName);
        txtFields.setText(stream);
    }

    public void upload_notes(View view) {

        Intent intent = new Intent(dashboard.this,upload_notes.class);
        startActivity(intent);
    }

    public void upload_asg(View view) {
        Intent intent = new Intent(dashboard.this,upload_files.class);
        startActivity(intent);
    }

    public void dash_not(View view) {
        Intent intent = new Intent(dashboard.this,dash_notes.class);
        startActivity(intent);
    }

    public void timepass(View view) {
    }

    public void upload_pdf(View view) {
        Intent intent = new Intent(dashboard.this,view_notes.class);
        startActivity(intent);
    }
/*
    public void logout(View view) {

         new storeUser(dashboard.this).removeUser();
         Intent intent = new Intent(dashboard.this,home.class);
         startActivity(intent);

    }*/
}

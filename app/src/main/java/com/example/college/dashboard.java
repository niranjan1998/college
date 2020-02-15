package com.example.college;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class dashboard extends AppCompatActivity {

    TextView txtName , txtFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtName = findViewById(R.id.txtUsername);
        txtFields = findViewById(R.id.txtField);

        showAllUserData();

    }

    private void showAllUserData() {

        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        String stream = intent.getStringExtra("stream");

        txtName.setText(userName);
        txtFields.setText(stream);
    }

}

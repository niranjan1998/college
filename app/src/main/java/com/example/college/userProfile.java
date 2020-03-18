package com.example.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class userProfile extends AppCompatActivity {

    TextView txt_userName, txt_class;
    EditText ed_roll, ed_email, ed_phone, ed_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txt_userName = findViewById(R.id.user_name);
        txt_class = findViewById(R.id.user_class);
        ed_roll = findViewById(R.id.ed_roll);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        ed_pass = findViewById(R.id.ed_password);

        showAllUserData();

    }

    public void showAllUserData() {

      /*  Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        String stream = intent.getStringExtra("stream");

        String roll = intent.getStringExtra("rol");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phoneNo");
        String password = intent.getStringExtra("password");


        txt_userName.setText(userName);
        txt_class.setText(stream);
        ed_roll.setText(roll);
        ed_email.setText(email);
        ed_phone.setText(phone);
        ed_pass.setText(password);*/

        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String name = result.getString("name", "");
        String roll = result.getString("roll", "");
        String email = result.getString("email", "");
        String stream = result.getString("class", "");
        String phone = result.getString("phone", "");
        String password = result.getString("password", "");
        //   String role = result.getString("role","");
        // String pic = result.getString("pic","");
        txt_userName.setText(name);
        txt_class.setText(stream);
        ed_roll.setText(roll);
        ed_email.setText(email);
        ed_phone.setText(phone);
        ed_pass.setText(password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(userProfile.this, dashboard.class);
        startActivity(intent);
    }
}

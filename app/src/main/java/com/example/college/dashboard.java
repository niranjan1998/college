package com.example.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class dashboard extends AppCompatActivity {

    TextView txtName, txtFields;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtName = findViewById(R.id.txtUsername);
        txtFields = findViewById(R.id.txtField);

        showAllUserData();

    }

    public void showAllUserData() {

     /*   Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        String stream = intent.getStringExtra("stream");


        txtName.setText(userName);
        txtFields.setText(stream);*/

        //   userLoginHelper helper = new userLoginHelper();
        //  txtName.setText(helper.getName());
        SharedPreferences result = getSharedPreferences("loginRef",MODE_PRIVATE);
        String name = result.getString("name","");
        String stream = result.getString("class","");
        txtName.setText(name);
        txtFields.setText(stream);

        //editor.putString("username", userEnteredRoll);

    }

    public void upload_notes(View view) {

        Intent intent = new Intent(dashboard.this, notes_view.class);
        startActivity(intent);
    }

    public void upload_asg(View view) {
        Intent intent = new Intent(dashboard.this, upload_files.class);
        startActivity(intent);
    }

    public void dash_not(View view) {
        Intent intent = new Intent(dashboard.this, dash_notes.class);
        startActivity(intent);
    }

    public void timepass(View view) {
        Intent intent = new Intent(dashboard.this, event_upload.class);
        startActivity(intent);
    }

    public void upload_pdf(View view) {
        Intent intent = new Intent(dashboard.this, view_notes.class);
        startActivity(intent);
    }

    public void logout(View view) {
        removeValue();

        Intent intent = new Intent(dashboard.this, home.class);
        startActivity(intent);
      //  finish();
    }

    private void removeValue() {
        editor = sharedPreferences.edit();
        sharedPreferences = getSharedPreferences("loginRef", MODE_PRIVATE);

        //save uses data in shared preference
        editor.putString("roll", " ");
        editor.putString("name", " ");
        editor.putString("email", " ");
        editor.putString("class", " ");
        editor.putString("phone", " ");
        editor.putString("password", " ");
        editor.putString("role", " ");
        editor.putString("pic", " ");
        editor.apply();
    }


    public void btn_profile(View view) {
        Intent intent = new Intent(dashboard.this, userProfile.class);
        startActivity(intent);
        finish();
    }
}

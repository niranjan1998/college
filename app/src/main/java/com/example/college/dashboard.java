package com.example.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    TextView txtName, txtFields;
    ImageView  profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtName = findViewById(R.id.txtUsername);
        txtFields = findViewById(R.id.txtField);
        profile_image = findViewById(R.id.dash_user_img);

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
        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String name = result.getString("name", "");
        String stream = result.getString("class", "");
        String roll = result.getString("roll", "");
        txtName.setText(name);
        txtFields.setText(stream);

        FirebaseDatabase.getInstance().getReference("UsersData")
                .child(roll).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userProfilePic = dataSnapshot.child("pic").getValue(String.class);
                try {
                    Glide.with(getApplicationContext())
                            .load(userProfilePic).into(profile_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //editor.putString("username", userEnteredRoll);

    }

    public void upload_notes(View view) {

        Intent intent = new Intent(dashboard.this, dash_notes.class);
        startActivity(intent);
    }

    public void upload_asg(View view) {
        Intent intent = new Intent(dashboard.this, upload_files.class);
        startActivity(intent);
    }

    public void chat(View view) {
        Intent intent = new Intent(dashboard.this, grp_chat.class);
        startActivity(intent);
    }

    public void event(View view) {
        Intent intent = new Intent(dashboard.this, event_upload.class);
        startActivity(intent);
    }
    public void myClass(View view) {
        Intent intent = new Intent(dashboard.this, my_class.class);
        startActivity(intent);
    }
    public void add_student(View view) {
        Intent intent = new Intent(dashboard.this, admin_panel.class);
        startActivity(intent);
    }
    public void chat_bot(View view) {
        Intent intent = new Intent(dashboard.this, chat_bot.class);
        startActivity(intent);
    }

    public void logout(View view) {
        removeValue();

        Intent intent = new Intent(dashboard.this, home.class);
        startActivity(intent);
        //  finish();
    }

    private void removeValue() {
        //removing values from sp
        getApplicationContext().getSharedPreferences("loginRef", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("loginUser", 0).edit().clear().apply();
        Intent intent = new Intent(dashboard.this, home.class);
        startActivity(intent);
        finish();
    }

    public void btn_profile(View view) {
        Intent intent = new Intent(dashboard.this, userProfile.class);
        startActivity(intent);
        finish();
    }

    public void show_grps(View view) {
        Intent intent = new Intent(dashboard.this, show_grp.class);
        startActivity(intent);
    }
}

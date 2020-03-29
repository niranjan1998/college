package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class show_grp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_grp);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Groups");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public void grp_mscSem1(View view) {
        Intent intent = new Intent(show_grp.this, grp_chat.class);
        startActivity(intent);
    }

    public void grp_mscSem2(View view) {
        Intent intent = new Intent(show_grp.this, grp_chat.class);
        startActivity(intent);
    }
}

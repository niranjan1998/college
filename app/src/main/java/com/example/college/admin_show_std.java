package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_show_std extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialToolbar materialToolbar;

    String i_role;
    Spinner spinner;
    String item;

    RecyclerView recyclerView;
    List<UserHelperClass> adminUserList;

    DatabaseReference databaseReference;
    ValueEventListener usersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_std);

        materialToolbar = findViewById(R.id.toll_bar);


        recyclerView = findViewById(R.id.recycle_view);

        Intent roles = getIntent();
        i_role = roles.getStringExtra("role");

        materialToolbar.setTitle( i_role + " Details");
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //spinner element
        spinner = findViewById(R.id.spinner);
        //spinner click listener
        spinner.setOnItemSelectedListener(this);
        //spinner drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(0, "Select Stream");
        if (i_role.toString().trim().equals("Student")) {
            categories.add("MSc IT 1");
            categories.add("MSc IT 2");
            categories.add("BSc IT 1");
            categories.add("BSc IT 2");
            categories.add("BSc IT 3");
        } else {
            categories.add("IT");
            categories.add("CS");
        }


        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        //Drop Down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attach data adapter
        spinner.setAdapter(dataAdapter);


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        showUsers();

        if (position != 0) {
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        }
    }

    public void showUsers() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(admin_show_std.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adminUserList = new ArrayList<>();

        final admin_user_adapter admin_user_adapter = new admin_user_adapter(admin_show_std.this, adminUserList);
        recyclerView.setAdapter(admin_user_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UsersData");
        /* usersListener = databaseReference.orderByChild("stream").equalTo(stream).addValueEventListener(new ValueEventListener() {
         */
        usersListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminUserList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    UserHelperClass userHelperClass = itemSnapshot.getValue(UserHelperClass.class);

                    assert userHelperClass != null;
                    if (userHelperClass.getStream().trim().equals(item.trim())) {
                        adminUserList.add(userHelperClass);
                    }
                }
                admin_user_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        recyclerView.setVisibility(View.GONE);
    }
}

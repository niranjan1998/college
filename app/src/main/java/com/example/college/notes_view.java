package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class notes_view extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("notebooks").child("BSC").child("sem6");//return the path to root
        reference
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //called for individual items in database reference..
                        String name = dataSnapshot.getKey(); //return the file name..
                        String url = dataSnapshot.getValue(String.class); //return url for filename..
                        ((notes_adapter) recyclerView.getAdapter()).update(name, url);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String name = dataSnapshot.getKey(); //return the file name..
                        String url = dataSnapshot.getValue(String.class); //return url for filename..
                        ((notes_adapter) recyclerView.getAdapter()).update(name, url);

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        //called for individual items in database reference..//return the file name..
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        notes_adapter myAdapter = new notes_adapter(recyclerView, getApplicationContext(), new ArrayList<String>(), new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);
    }

    public void btn_upload_notes(View view) {
        Intent intent = new Intent(notes_view.this, view_notes.class);
        startActivity(intent);
    }
}

package com.example.college;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class my_class extends AppCompatActivity {
    PhotoView class_image;

    RecyclerView recyclerView;
    List<UserHelperClass> usersList;

    DatabaseReference databaseReference;
    ValueEventListener usersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingTollBar);
        collapsingToolbarLayout.setTitle("Collapse TollBar");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));

        class_image = findViewById(R.id.class_image);

        recyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(my_class.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        usersList = new ArrayList<>();

        final my_class_adapter class_adapter = new my_class_adapter(my_class.this, usersList);
        recyclerView.setAdapter(class_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UsersData");

        usersListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    UserHelperClass userHelperClass = itemSnapshot.getValue(UserHelperClass.class);
                    usersList.add(userHelperClass);
                }
                class_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

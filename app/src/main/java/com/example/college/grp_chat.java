package com.example.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class grp_chat extends AppCompatActivity {

    EditText txt_msg;

    private long last_click = 0;

    //for getting comment data
    RecyclerView recyclerView;
    List<grp_model> msgList;
    DatabaseReference dbReference;
    ValueEventListener msgListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_chat);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txt_msg = findViewById(R.id.txt_msg);

        //to show comments
        recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(grp_chat.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        msgList = new ArrayList<>();
        final grp_adapter g_adapter = new grp_adapter(grp_chat.this, msgList);


        recyclerView.setAdapter(g_adapter);

        dbReference = FirebaseDatabase.getInstance().getReference("Groups").child("MSc IT 2");

        msgListener = dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    grp_model g_model = itemSnapshot.getValue(grp_model.class);
                    msgList.add(g_model);
                }
                g_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void open_class(View view) {

        Intent intent = new Intent(grp_chat.this, my_class.class);
        startActivity(intent);
    }

    public void btn_uploadMsg(View view) {
        if (SystemClock.elapsedRealtime() - last_click < 1000) {
            return;
        }
        last_click = SystemClock.elapsedRealtime();

        String name;
        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        name = result.getString("name", "");
        String myCurrentDate = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        grp_model g_model = new grp_model(
                name,
                txt_msg.getText().toString(),
                myCurrentDate
        );
        FirebaseDatabase.getInstance().getReference("Groups").child("MSc IT 2")
                .child(myCurrentDate).setValue(g_model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(grp_chat.this, "Added", Toast.LENGTH_SHORT).show();
                    txt_msg.setText("");
                   /* startActivity(getIntent());
                    finish();
                    overridePendingTransition(0, 0);*/
                }
            }
        });

    }
}

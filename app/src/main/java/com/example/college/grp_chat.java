package com.example.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class grp_chat extends AppCompatActivity {

    EditText txt_msg;
    TextView txt_className;
    ImageView imageView;

    private long last_click = 0;

    //for getting comment data
    RecyclerView recyclerView;
    List<grp_model> msgList;
    DatabaseReference dbReference;
    ValueEventListener msgListener;

    String grp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_chat);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        setSupportActionBar(materialToolbar);


        txt_msg = findViewById(R.id.txt_msg);
        txt_className = findViewById(R.id.class_name);
        imageView = findViewById(R.id.class_image);


        //getting semester name
        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String sp_stream = result.getString("role", "");
        String sp_class = result.getString("class", "");


        Intent intent = getIntent();
        String i_stream = intent.getStringExtra("group");

        if (sp_stream.trim().equals("Teacher")) {
            grp_name = i_stream;
            showMessages();
            Toast.makeText(getApplicationContext(), "Teacher i_stream", Toast.LENGTH_SHORT).show();
        } else {
            grp_name = sp_class;
            showMessages();
            Toast.makeText(getApplicationContext(), "sp_stream", Toast.LENGTH_SHORT).show();
        }

        txt_className.setText(grp_name);


        FirebaseDatabase.getInstance().getReference("Groups")
                .child(grp_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userProfilePic = dataSnapshot.child("pic").getValue(String.class);
                try {
                    Glide.with(getApplicationContext())
                            .load(userProfilePic).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //grp_name = sp_stream;

        //to store group name in sp
        SharedPreferences sp_grp_name = getSharedPreferences("spGrpName", MODE_PRIVATE);
        sp_grp_name.edit().putString("name", grp_name).apply();
        String grp_names = sp_grp_name.getString("name", "");

    }

    private void showMessages() {

        //to show msg
        recyclerView = findViewById(R.id.recycle_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(grp_chat.this);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        msgList = new ArrayList<>();
        final grp_adapter g_adapter = new grp_adapter(grp_chat.this, msgList);


        recyclerView.setAdapter(g_adapter);

        dbReference = FirebaseDatabase.getInstance().getReference("Groups").child(grp_name).child("messages");

        msgListener = dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    grp_model g_model = itemSnapshot.getValue(grp_model.class);
                    msgList.add(g_model);
                }
                g_adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(msgList.size() - 1);

                //to get last position when keyboard open
                recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom < oldBottom) {
                            final int lai = g_adapter.getItemCount() - 1;
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    int offset = -1000000;
                                    View bottomView = linearLayoutManager.findViewByPosition(lai);
                                    if (bottomView != null) {
                                        offset = 0 - bottomView.getHeight();
                                    }
                                    linearLayoutManager.scrollToPositionWithOffset(lai, offset);
                                }
                            });
                        }
                    }
                });
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
        FirebaseDatabase.getInstance().getReference("Groups").child(grp_name).child("messages")
                .child(myCurrentDate).setValue(g_model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(grp_chat.this, "Added", Toast.LENGTH_SHORT).show();
                    txt_msg.setText("");
                }
            }
        });
    }
}

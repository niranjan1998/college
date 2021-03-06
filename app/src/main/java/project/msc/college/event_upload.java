package project.msc.college;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class event_upload extends AppCompatActivity {

    RecyclerView recyclerView;
    List<event_model> event_models_list;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ProgressDialog progressDialog;
    MaterialToolbar materialToolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload);

        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Events");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);

        SharedPreferences result = getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String role = result.getString("role", "");

        if (role.equals("Teacher")) {
            fab.setVisibility(View.VISIBLE);
        }

        recyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(event_upload.this, 1);
        //  gridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events ....");

        event_models_list = new ArrayList<>();

        final event_adapter e_adapter = new event_adapter(event_upload.this, event_models_list);
        recyclerView.setAdapter(e_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event_models_list.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    event_model e_model = itemSnapshot.getValue(event_model.class);
                    e_model.setKey(itemSnapshot.getKey());
                    event_models_list.add(e_model);
                }
                e_adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }

    public void btn_uploadEvent(View view) {

        startActivity(new Intent(this, event_upload_event.class));
    }
}

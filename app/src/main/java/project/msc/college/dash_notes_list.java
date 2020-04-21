package project.msc.college;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class dash_notes_list extends AppCompatActivity {

    TextView tv_main, tv_sem;

    String Stream_path;
    String Sem_path;
    String extra;

    RecyclerView recyclerView;
    List<dash_notes_model> notes_models_list;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_notes_list);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);

//getting stream and semester
        tv_sem = findViewById(R.id.tv_sem);
        tv_main = findViewById(R.id.tv_main);

        Intent intent = getIntent();
        String main = intent.getStringExtra("main");
        String sem = intent.getStringExtra("sem");
        extra = intent.getStringExtra("extra");


        materialToolbar.setTitle(extra);
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tv_main.setText(main);
        tv_sem.setText(sem);

        Stream_path = tv_main.getText().toString();
        Sem_path = tv_sem.getText().toString();

        //showing data of notes
        recyclerView= findViewById(R.id.recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(dash_notes_list.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading notes ....");

        notes_models_list = new ArrayList<>();

        final dash_notes_adapter n_adapter = new dash_notes_adapter(dash_notes_list.this,notes_models_list);
        recyclerView.setAdapter(n_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("storeBooks").child(Stream_path).child(Sem_path).child(extra);

        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes_models_list.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    dash_notes_model n_model = itemSnapshot.getValue(dash_notes_model.class);
                    notes_models_list.add(n_model);
                }
                n_adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    public void btn_upload_notes(View view) {

        Intent send_class = new Intent(getApplicationContext(), dash_notes_upload.class);
        send_class.putExtra("class", Stream_path);
        send_class.putExtra("sem", Sem_path);
        send_class.putExtra("extra", extra);
        startActivity(send_class);

    }
}

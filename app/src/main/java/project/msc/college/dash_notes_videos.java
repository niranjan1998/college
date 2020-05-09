package project.msc.college;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class dash_notes_videos extends AppCompatActivity {

    MaterialToolbar materialToolbar;
    ExtendedFloatingActionButton fab_upload;
    String main, sem, extra;
    String video_id;
    StorageReference storage;
    DatabaseReference database;

    RecyclerView recyclerView;
    List<dash_notes_model> notes_models_list;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_notes_videos);

        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Video Notes");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        main = intent.getStringExtra("main");
        sem = intent.getStringExtra("sem");
        extra = intent.getStringExtra("extra");


        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference("storeBooks");

        /*YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_view);
        getLifecycle().addObserver(youTubePlayerView);
        */

        fab_upload = findViewById(R.id.fab_upload);

        //showing data of notes
        recyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(dash_notes_videos.this, 1);
        gridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Videos ....");

        notes_models_list = new ArrayList<>();

        final dash_notes_video_adapter n_adapter = new dash_notes_video_adapter(dash_notes_videos.this, notes_models_list);
        recyclerView.setAdapter(n_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("storeBooks").child(main).child(sem).child(extra);

        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes_models_list.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    dash_notes_model n_model = itemSnapshot.getValue(dash_notes_model.class);
                    notes_models_list.add(n_model);

                }
                n_adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(notes_models_list.size() - 1);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

//upload videos
        fab_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(dash_notes_videos.this)
                        .setTitle("Share YouTube Video");
                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_video, null);

                final EditText ed_title = view.findViewById(R.id.ed_title);
                final EditText ed_url = view.findViewById(R.id.ed_url);
                MaterialButton btn_cancel = view.findViewById(R.id.cancel_button);
                MaterialButton btn_upload = view.findViewById(R.id.upload_video);

                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = ed_title.getText().toString();
                        String url = ed_url.getText().toString();

                        if (name.trim().isEmpty()) {
                            ed_title.setError("Required");
                        } else if (url.trim().isEmpty()) {
                            ed_url.setError("Required");
                        } else {
                            try {

                                if (url.trim().length() > 11) {
                                    video_id = url.substring(url.length() - 11);
                                } else {
                                    video_id = url;
                                }
                                String key = database.push().getKey();
                                dash_notes_model upload = new dash_notes_model(ed_title.getText().toString(), video_id, key, main, extra, sem);
                                assert key != null;
                                database.child(main).child(sem).child(extra)
                                        .child(key).setValue(upload)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(dash_notes_videos.this, "Video Added", Toast.LENGTH_LONG).show();
                                                alertDialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(dash_notes_videos.this, "Failed to add", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(dash_notes_videos.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }
}

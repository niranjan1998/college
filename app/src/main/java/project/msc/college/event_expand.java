package project.msc.college;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class event_expand extends AppCompatActivity {

    TextView event_expand_dep;
    TextView event_expand_name;
    TextView txt_comments;
    ImageView event_expand_img;
    TextView event_expand_date;
    EditText ed_comment;
    String key = "";
    String imgUrl = "";
    DatabaseReference databaseReference;
    Button button;


    //for getting comment data
    RecyclerView recyclerView;
    List<comment_model> comment_model_list;
    DatabaseReference dbReference;
    ValueEventListener commentListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_expand);

        event_expand_dep = findViewById(R.id.tv_expand_dep);
        event_expand_img = findViewById(R.id.event_expand_img);
        event_expand_name = findViewById(R.id.tv_expand_title);
        event_expand_date = findViewById(R.id.tv_expand_date);
        ed_comment = findViewById(R.id.ed_comment);
        button = findViewById(R.id.button);
        txt_comments = findViewById(R.id.txt_comments);

        SharedPreferences result = getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String role = result.getString("role", "");

        if (role.equals("Teacher")) {
            button.setVisibility(View.VISIBLE);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            key = bundle.getString("keyValue");
            imgUrl = bundle.getString("image");
            event_expand_name.setText(bundle.getString("title"));
            //event_expand_img.setImageResource(bundle.getInt("image"));
            Glide.with(this).load(bundle.getString("image")).into(event_expand_img);
            event_expand_dep.setText(bundle.getString("dep"));
            event_expand_date.setText(bundle.getString("date"));
        }

        //to show comments
        recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(event_expand.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        comment_model_list = new ArrayList<>();
        final event_comment_adapter ec_adapter = new event_comment_adapter(event_expand.this, comment_model_list);
        recyclerView.setAdapter(ec_adapter);

        dbReference = FirebaseDatabase.getInstance().getReference("Events").child(key).child("comments");

        commentListener = dbReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_model_list.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    comment_model c_model = itemSnapshot.getValue(comment_model.class);
                    assert c_model != null;
                    c_model.setKey(itemSnapshot.getKey());
                    comment_model_list.add(c_model);
                }
                ec_adapter.notifyDataSetChanged();


                txt_comments.setText("Comments : " + " " + ec_adapter.getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void btnDeleteEvent(View view) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imgUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(key).removeValue();
                Toast.makeText(event_expand.this, "Event Deleted", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(getApplicationContext(), event_upload.class));
                overridePendingTransition(0, 0);

            }
        });
    }

    public void btn_comment(View view) {

        String name, stream;
        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        name = result.getString("name", "");
        stream = result.getString("class", "");
        String myCurrentDate = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());
        if (!ed_comment.getText().toString().trim().isEmpty()) {
            comment_model c_model = new comment_model(
                    name,
                    stream,
                    ed_comment.getText().toString(),
                    myCurrentDate
            );
            FirebaseDatabase.getInstance().getReference("Events").child(key).child("comments")
                    .child(myCurrentDate).setValue(c_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ed_comment.setText(" ");
                        Toast.makeText(event_expand.this, "Comment Added", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }
            });
        }
    }

    public void txt_comment(View view) {
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void close_comment(View view) {
        recyclerView.setVisibility(View.GONE);
    }

    public void save_img(View view) {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            imgUrl = bundle.getString("image");
        }

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(imgUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        assert downloadManager != null;
        long ref = downloadManager.enqueue(request);

        Toast.makeText(this, "File Downloading", Toast.LENGTH_SHORT).show();

    }
}

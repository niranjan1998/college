package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class event_expand extends AppCompatActivity {

    TextView event_expand_dep;
    TextView event_expand_name;
    ImageView event_expand_img;
    TextView event_expand_date;
    String key="";
    String imgUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_expand);

        event_expand_dep= findViewById(R.id.tv_expand_dep);
        event_expand_img = findViewById(R.id.event_expand_img);
        event_expand_name = findViewById(R.id.tv_expand_title);
        event_expand_date = findViewById(R.id.tv_expand_date);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            key = bundle.getString("keyValue");
            imgUrl = bundle.getString("image");
            event_expand_name.setText(bundle.getString("title"));
            //event_expand_img.setImageResource(bundle.getInt("image"));
            Glide.with(this).load(bundle.getString("image")).into(event_expand_img);
            event_expand_dep.setText(bundle.getString("dep"));
            event_expand_date.setText(bundle.getString("date"));
        }
    }

    public void btnDeleteEvent(View view) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imgUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(key).removeValue();
                Toast.makeText(event_expand.this,"Event Deleted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),event_upload.class));
                finish();
            }
        });
    }
}

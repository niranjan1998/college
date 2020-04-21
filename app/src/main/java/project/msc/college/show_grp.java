package project.msc.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class show_grp extends AppCompatActivity {

    ImageView img_mscSem1, img_mscSem2, img_bscSem1, img_bscSem2, img_bscSem3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_grp);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Groups");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        img_mscSem1 = findViewById(R.id.grp_imgSem1);
        img_mscSem2 = findViewById(R.id.grp_imgSem2);
        img_bscSem1 = findViewById(R.id.grp_imgSemB1);
        img_bscSem2 = findViewById(R.id.grp_imgSemB2);
        img_bscSem3 = findViewById(R.id.grp_imgSemB3);


        FirebaseDatabase.getInstance().getReference("Groups")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String img_mscS1 = dataSnapshot.child("MSc IT 1").child("pic").getValue(String.class);
                        String img_mscS2 = dataSnapshot.child("MSc IT 2").child("pic").getValue(String.class);
                        String img_bscS1 = dataSnapshot.child("BSc IT 1").child("pic").getValue(String.class);
                        String img_bscS2 = dataSnapshot.child("BSc IT 2").child("pic").getValue(String.class);
                        String img_bscS3 = dataSnapshot.child("BSc IT 3").child("pic").getValue(String.class);
                        try {
                            Glide.with(getApplicationContext()).load(img_mscS1).into(img_mscSem1);
                            Glide.with(getApplicationContext()).load(img_mscS2).into(img_mscSem2);
                            Glide.with(getApplicationContext()).load(img_bscS1).into(img_bscSem1);
                            Glide.with(getApplicationContext()).load(img_bscS2).into(img_bscSem2);
                            Glide.with(getApplicationContext()).load(img_bscS3).into(img_bscSem3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void grp_mscSem1(View view) {
        Intent intent = new Intent(getApplicationContext(), grp_chat.class);
        intent.putExtra("group", "MSc IT 1");
        startActivity(intent);
    }

    public void grp_mscSem2(View view) {
        Intent intent = new Intent(getApplicationContext(), grp_chat.class);
        intent.putExtra("group", "MSc IT 2");
        startActivity(intent);
    }

    public void grp_bscSem1(View view) {
        Intent intent = new Intent(getApplicationContext(), grp_chat.class);
        intent.putExtra("group", "BSc IT 1");
        startActivity(intent);
    }

    public void grp_bscSem2(View view) {
        Intent intent = new Intent(getApplicationContext(), grp_chat.class);
        intent.putExtra("group", "BSc IT 2");
        startActivity(intent);
    }

    public void grp_bscSem3(View view) {
        Intent intent = new Intent(getApplicationContext(), grp_chat.class);
        intent.putExtra("group", "BSc IT 3");
        startActivity(intent);
    }
}

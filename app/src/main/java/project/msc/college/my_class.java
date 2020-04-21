package project.msc.college;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class my_class extends AppCompatActivity {

    PhotoView class_image;
    Uri uri;
    String imageUrl;
    String grp_names;


    RecyclerView recyclerView;
    List<UserHelperClass> usersList;

    DatabaseReference databaseReference;
    ValueEventListener usersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingTollBar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        class_image = findViewById(R.id.class_image);

        SharedPreferences result = getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        final String stream = result.getString("class", "");

        recyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(my_class.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        usersList = new ArrayList<>();

        final my_class_adapter class_adapter = new my_class_adapter(my_class.this, usersList);
        recyclerView.setAdapter(class_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UsersData");
        /* usersListener = databaseReference.orderByChild("stream").equalTo(stream).addValueEventListener(new ValueEventListener() {
         */
        usersListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    UserHelperClass userHelperClass = itemSnapshot.getValue(UserHelperClass.class);

                    assert userHelperClass != null;
                    if (userHelperClass.getStream().equals(grp_names) || userHelperClass.getRole().equals("Teacher")) {
                        usersList.add(userHelperClass);
                    }
                }
                class_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //to store group name in sp
        SharedPreferences sp_grp_name = getSharedPreferences("spGrpName", MODE_PRIVATE);
        grp_names = sp_grp_name.getString("name", "");

        collapsingToolbarLayout.setTitle(grp_names);

        FirebaseDatabase.getInstance().getReference("Groups")
                .child(grp_names).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userProfilePic = dataSnapshot.child("pic").getValue(String.class);
                try {
                    Glide.with(getApplicationContext())
                            .load(userProfilePic).into(class_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void btnSelectImage(View view) {

        Intent imgPicker = new Intent(Intent.ACTION_PICK);
        imgPicker.setType("image/*");
        startActivityForResult(imgPicker, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            assert data != null;
            uri = data.getData();
            class_image.setImageURI(uri);
            uploadImage();

        } else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage() {


        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("Groups").child(grp_names).child("pic").child(Objects.requireNonNull(uri.getLastPathSegment()));

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                assert urlImage != null;
                imageUrl = urlImage.toString();
                FirebaseDatabase.getInstance().getReference("Groups")
                        .child(grp_names).child("pic").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(my_class.this, "Image Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(my_class.this, "Image not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

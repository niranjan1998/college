package project.msc.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class userProfile extends AppCompatActivity {

    TextView txt_userName, txt_class;
    EditText ed_roll, ed_email, ed_phone, ed_pass, ed_c_pass;
    String name, roll, email, stream, phone, password;

    TextInputLayout txt_editPassword;
    Button btn_update;
    MaterialButton btn_logout, btn_darkMode;
    MaterialToolbar materialToolbar;

    ImageView profile_image, full_image;

    Uri uri;
    String imageUrl;

    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isNightModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference("UsersData");

        txt_userName = findViewById(R.id.user_name);
        txt_class = findViewById(R.id.user_class);
        ed_roll = findViewById(R.id.ed_roll);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        ed_pass = findViewById(R.id.ed_password);
        ed_c_pass = findViewById(R.id.ed_c_password);
        profile_image = findViewById(R.id.user_profile);
        full_image = findViewById(R.id.full_image);

        txt_editPassword = findViewById(R.id.txt_editPassword);
        btn_update = findViewById(R.id.update_password);
        btn_darkMode = findViewById(R.id.btn_darkMode);

        sharedPreferences = getSharedPreferences("system", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isNightModeOn = sharedPreferences.getBoolean("NightMode", false);


        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btn_darkMode.setText(R.string.light_mode);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btn_darkMode.setText(R.string.night_mode);
        }

        btn_darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNightModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("NightMode", false);
                    editor.apply();

                    btn_darkMode.setText(R.string.night_mode);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("NightMode", true);
                    editor.apply();

                    btn_darkMode.setText(R.string.light_mode);

                }
            }
        });
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingTollBar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);
        collapsingToolbarLayout.setTitle("Profile");


        materialToolbar = findViewById(R.id.toll_bar_name);
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        showAllUserData();

    }

    public void showAllUserData() {

      /*  Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        String stream = intent.getStringExtra("stream");

        String roll = intent.getStringExtra("rol");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phoneNo");
        String password = intent.getStringExtra("password");

        txt_userName.setText(userName);
        txt_class.setText(stream);
        ed_roll.setText(roll);
        ed_email.setText(email);
        ed_phone.setText(phone);
        ed_pass.setText(password);*/

        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        name = result.getString("name", "");
        roll = result.getString("roll", "");
        email = result.getString("email", "");
        stream = result.getString("class", "");
        phone = result.getString("phone", "");
        password = result.getString("password", "");
        //   String role = result.getString("role","");
        txt_userName.setText(name);
        txt_class.setText(stream);
        ed_roll.setText(roll);
        ed_email.setText(email);
        ed_phone.setText(phone);
        ed_pass.setText(password);

        FirebaseDatabase.getInstance().getReference("UsersData")
                .child(roll).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userProfilePic = dataSnapshot.child("pic").getValue(String.class);
                try {
                    Glide.with(getApplicationContext())
                            .load(userProfilePic).into(profile_image);
                    Glide.with(getApplicationContext())
                            .load(userProfilePic).into(full_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(userProfile.this, dashboard.class);
        startActivity(intent);
    }

    public void updatePassword(View view) {
        if (isPasswordChanged()) {
            Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show();
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        } else {
            Toast.makeText(this, "Password Not Changed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPasswordChanged() {

        if (!ed_pass.getText().toString().equals(ed_c_pass.getText().toString())) {
            String password = ed_c_pass.getText().toString();

            if (password.trim().length() < 5) {
                ed_c_pass.setError("Weak password");
            } else {

                databaseReference.child(roll).child("password").setValue(ed_c_pass.getText().toString());

                //change sp value all
                SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
                result.edit().putString("password", ed_c_pass.getText().toString()).apply();
                //change sp value of login screen
                SharedPreferences login = getSharedPreferences("loginUser", MODE_PRIVATE);
                login.edit().putString("password", ed_c_pass.getText().toString()).apply();
                return true;
            }
        }
        return false;
    }

    public void select_image(View view) {

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
            profile_image.setImageURI(uri);


            uploadImage();

        } else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }

    }

    public void uploadImage() {

        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("UsersData").child(roll).child("pic").child(Objects.requireNonNull(uri.getLastPathSegment()));

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                assert urlImage != null;
                imageUrl = urlImage.toString();

                FirebaseDatabase.getInstance().getReference("UsersData")
                        .child(roll).child("pic").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(userProfile.this, "Image not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void show_edit(View view) {
        txt_editPassword.setVisibility(View.VISIBLE);
        btn_update.setVisibility(View.VISIBLE);
    }

    public void logout() {


        //removing values from sp
        getApplicationContext().getSharedPreferences("loginRef", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("loginUser", 0).edit().clear().apply();

        Toast.makeText(getApplicationContext(), "Logout Done", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(userProfile.this, home.class);
        startActivity(intent);
        finish();

    }

    public void feedback(View view) {

        Intent intent = new Intent(userProfile.this, feedback.class);
        startActivity(intent);

    }

    public void shareApp(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String send_test = "https://www.amazon.com/dp/B087BJZ7KY/ref=apps_sf_sta";

        intent.putExtra(Intent.EXTRA_TEXT, send_test);
        //   Get College App from the Amazon Appstore. Check it out - https://www.amazon.com/dp/B087BJZ7KY/ref=apps_sf_sta

        startActivity(Intent.createChooser(intent, "Share Using"));

    }

/*
    private boolean removeValue() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Do you want to Logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //removing values from sp
                        getApplicationContext().getSharedPreferences("loginRef", 0).edit().clear().apply();
                        getApplicationContext().getSharedPreferences("loginUser", 0).edit().clear().apply();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Confirm");
        dialog.show();
        return true;
    }
*/
}

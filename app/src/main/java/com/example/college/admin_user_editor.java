package com.example.college;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin_user_editor extends AppCompatActivity {

    EditText ed_name, ed_email, ed_phone, ed_roll, ed_password;
    String s_name, s_email, s_phone, s_roll, s_password, s_class;
    TextView txt_userName, txt_class;
    ImageView imageView;
    Button btn_img_del, btn_update;

    DatabaseReference databaseReference;

    MaterialToolbar materialToolbar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_editor);

        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Student Details");
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed_name = findViewById(R.id.ed_fullName);
        ed_email = findViewById(R.id.ed_email);
        ed_roll = findViewById(R.id.ed_roll);
        ed_phone = findViewById(R.id.ed_phone);
        ed_password = findViewById(R.id.ed_password);
        imageView = findViewById(R.id.user_profile);
        btn_img_del = findViewById(R.id.btn_del_img);
        btn_update = findViewById(R.id.btn_update);

        txt_userName = findViewById(R.id.user_name);
        txt_class = findViewById(R.id.user_class);

        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            ed_name.setText(bundle.getString("name"));
            txt_userName.setText(bundle.getString("name"));
            txt_class.setText(bundle.getString("class"));
            ed_roll.setText(bundle.getString("roll"));
            ed_email.setText(bundle.getString("email"));
            ed_phone.setText(bundle.getString("phone"));
            ed_password.setText(bundle.getString("password"));
            Glide.with(this).load(bundle.getString("image")).into(imageView);

        }
    }

    public void btn_del_img(View view) {

        final String profile = "https://firebasestorage.googleapis.com/v0/b/college-36833.appspot.com/o/addprofile.jpeg?alt=media&token=eb2dde58-e796-491f-9183-13d8f469579c";
        FirebaseDatabase.getInstance().getReference("UsersData")
                .child(ed_roll.getText().toString().trim()).child("pic").setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(admin_user_editor.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                    Glide.with(getApplicationContext()).load(profile).into(imageView);
                } else {
                    Toast.makeText(admin_user_editor.this, "Image not deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validateField() {

        s_name = ed_name.getText().toString();
        s_email = ed_email.getText().toString();
        s_phone = ed_phone.getText().toString();
        s_roll = ed_roll.getText().toString();
        s_password = ed_password.getText().toString();
        s_class = txt_class.getText().toString();

        if (s_name.trim().equals("")) {
            ed_name.setError("Enter Name");
        } else if (s_roll.trim().equals("")) {
            ed_roll.setError("Enter Roll no");
        } else if (s_phone.trim().equals("")) {
            ed_phone.setError("Enter Phone no");
        } else if (s_phone.trim().length() < 10) {
            ed_phone.setError("Incorrect Phone no");
        } else if (s_password.trim().equals("")) {
            ed_password.setError("Enter Password");
        } else if (s_password.trim().length() < 5) {
            ed_password.setError("short password");
        } else if (s_email.trim().equals("")) {
            ed_email.setError("Enter e-mail");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(s_email).matches()) {
            ed_email.setError("Not a valid e-mail");
        } else {
            String profile = "https://firebasestorage.googleapis.com/v0/b/college-36833.appspot.com/o/addprofile.jpeg?alt=media&token=eb2dde58-e796-491f-9183-13d8f469579c";
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("UsersData");
            UserHelperClass helperClass = new UserHelperClass(s_name, s_roll, s_phone, s_email, s_password, s_class, "Student", profile );
            reference.child(s_roll).setValue(helperClass);
            txt_userName.setText(s_name);
            Toast.makeText(getApplicationContext(), "Data Changed", Toast.LENGTH_SHORT).show();
        }
    }


    public void btn_update(View view) {
        validateField();
    }
}

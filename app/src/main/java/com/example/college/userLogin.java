package com.example.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class userLogin extends AppCompatActivity {

    Button isUser;
    EditText username, password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean saveLogin;
    CheckBox sl_checkbox;

    TextInputLayout v_username, v_password;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);


        //All elements Hooks
        v_username = findViewById(R.id.user_email);
        v_password = findViewById(R.id.user_password);
        isUser = findViewById(R.id.btn_login);

        username = findViewById(R.id.user_emails);
        password = findViewById(R.id.user_passwords);
        progressBar = findViewById(R.id.progressBar);

        int pv = progressBar.getProgress();


        sharedPreferences = getSharedPreferences("loginRef", MODE_PRIVATE);
        sl_checkbox = findViewById(R.id.checkbox);
        editor = sharedPreferences.edit();

        isUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        saveLogin = sharedPreferences.getBoolean("saveLogin", true);
        if (saveLogin == true) {
            username.setText(sharedPreferences.getString("roll", null));
            password.setText(sharedPreferences.getString("password", null));
        }
    }

    public Boolean validateUsername() {
        String val = v_username.getEditText().getText().toString();

        if (val.isEmpty()) {
            v_username.setError("Field cannot be empty");
            return false;
        } else {
            v_username.setError(null);
            v_username.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = v_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            v_password.setError("Field cannot be empty");
            return false;
        } else {
            v_password.setError(null);
            v_password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser() {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;

        } else {
            isUser();
        }

    }

    public void isUser() {
        final String userEnteredRoll = username.getText().toString().trim();
        final String userEnteredPassword = password.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("studentData");

        Query checkUser = reference.orderByChild("roll").equalTo(userEnteredRoll);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    v_username.setError(null);
                    v_username.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        v_password.setError(null);
                        v_password.setErrorEnabled(false);

//transfer data to dashboard
                        String nameFromDB = dataSnapshot.child(userEnteredRoll).child("name").getValue(String.class);
                        String rollFromDB = dataSnapshot.child(userEnteredRoll).child("roll").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredRoll).child("phone").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredRoll).child("email").getValue(String.class);
                        String streamFromDB = dataSnapshot.child(userEnteredRoll).child("stream").getValue(String.class);
                        String passFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);
                        String roleFromDB = dataSnapshot.child(userEnteredRoll).child("role").getValue(String.class);
                        String picFromDB = dataSnapshot.child(userEnteredRoll).child("pic").getValue(String.class);

                        //save uses data in shared preference
                        if (sl_checkbox.isChecked()) {
                            editor.putBoolean("saveLogin", true);
                            editor.putString("roll", rollFromDB);
                            editor.putString("name", nameFromDB);
                            editor.putString("email", emailFromDB);
                            editor.putString("class", streamFromDB);
                            editor.putString("phone", phoneNoFromDB);
                            editor.putString("password", passFromDB);
                            editor.putString("role", roleFromDB);
                            editor.putString("pic", picFromDB);

                            editor.commit();
                        }


                        userLoginHelper helper = new userLoginHelper();

                        // helper.getName(nameFromDB);
                        //    helper.setName(nameFromDB);
                        // System.out.println(helper.getName());
                        //holder.notes_names.setText(notesList.get(i).getName());
                        //reference.child(roll).setValue(helperClass);


                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
                        //Intent intent = new Intent(getApplicationContext(), userProfile.class);
/*
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("roll", rollFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("stream", streamFromDB);
                        intent.putExtra("password", passFromDB);*/
/*
//store user data using shared preference
                        storeUser storeUser = new storeUser(userLogin.this);
                        storeUser.setName(userEnteredRoll);
                        storeUser.setPass(passFromDB);
*/
                        startActivity(intent);

                    } else {
                        v_password.setError("Wrong Password");
                        v_password.requestFocus();
                    }
                } else {
                    v_username.setError("No such User exist");
                    v_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
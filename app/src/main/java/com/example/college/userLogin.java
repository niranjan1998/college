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
    CheckBox slcheckbox;

    TextInputLayout vusername,vpassword;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);


        //All elements Hooks
        vusername = findViewById(R.id.user_email);
        vpassword = findViewById(R.id.user_password);
        isUser = findViewById(R.id.btn_login);

        username = findViewById(R.id.user_emails);
        password = findViewById(R.id.user_passwords);
        progressBar = findViewById(R.id.progressBar);

        int pv = progressBar.getProgress();


        sharedPreferences = getSharedPreferences("loginRef", MODE_PRIVATE);
        slcheckbox = findViewById(R.id.checkbox);
        editor = sharedPreferences.edit();

        isUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUser();

            }
        });

       saveLogin = sharedPreferences.getBoolean("saveLogin",true);
        if(saveLogin==true){
            username.setText(sharedPreferences.getString("username",null));
            password.setText(sharedPreferences.getString("password",null));

        }


    }

    public Boolean validateUsername() {
        String val = vusername.getEditText().getText().toString();

        if (val.isEmpty()) {
            vusername.setError("Field cannot be empty");
            return false;
        } else {
            vusername.setError(null);
            vusername.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = vpassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            vpassword.setError("Field cannot be empty");
            return false;
        } else {
            vpassword.setError(null);
            vpassword.setErrorEnabled(false);
            return true;
        }
    }


    public void loginUser() {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {

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

                    vusername.setError(null);
                    vusername.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        vpassword.setError(null);
                        vpassword.setErrorEnabled(false);

                        if (slcheckbox.isChecked()) {
                            editor.putBoolean("saveLogin", true);
                            editor.putString("username", userEnteredRoll);
                            editor.putString("password", userEnteredPassword);
                            editor.commit();
                        }


//transfer data to dashboard
                        String nameFromDB = dataSnapshot.child(userEnteredRoll).child("name").getValue(String.class);
                        String rollFromDB = dataSnapshot.child(userEnteredRoll).child("roll").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredRoll).child("phone").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredRoll).child("email").getValue(String.class);
                        String streamFromDB = dataSnapshot.child(userEnteredRoll).child("stream").getValue(String.class);
                        String passFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), dashboard.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("roll", rollFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("stream", streamFromDB);
                        intent.putExtra("password", passFromDB);
/*
//store user data using shared preference
                        storeUser storeUser = new storeUser(userLogin.this);
                        storeUser.setName(userEnteredRoll);
                        storeUser.setPass(passFromDB);
*/
                        startActivity(intent);

                    } else {
                        vpassword.setError("Wrong Password");
                        vpassword.requestFocus();
                    }
                } else {
                    vusername.setError("No such User exist");
                    vusername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
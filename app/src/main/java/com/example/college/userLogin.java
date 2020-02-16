package com.example.college;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;


import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class userLogin extends AppCompatActivity {

    Button login_btn;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);


        //All elements Hooks
        username = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        login_btn = findViewById(R.id.btn_login);

    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


    public void loginUser(View view) {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser(view);
        }

    }

    public void isUser(View view) {
        final String userEnteredRoll = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("studentData");

        Query checkUser = reference.orderByChild("roll").equalTo(userEnteredRoll);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        password.setError(null);
                        password.setErrorEnabled(false);


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

//store user data using shared preference
                        storeUser storeUser = new storeUser(userLogin.this);
                        storeUser.setName(nameFromDB);
                        storeUser.setPass(passFromDB);

                        startActivity(intent);

                    } else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    username.setError("No such User exist");
                    username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
  /*  //Call SignUp Screen
    public void callSignUpScreen(View view) {
        //To call next activity
        Intent intent = new Intent(Login.this, SignUp.class);

        //create pairs for animation
        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair&lt;View, String&gt;(image, "logo_image"); //1st one is the element and 2nd is the transition name of animation.
        pairs[1] = new Pair&lt;View, String&gt;(logoText, "logo_text");
        pairs[2] = new Pair&lt;View, String&gt;(sloganText, "logo_desc");
        pairs[3] = new Pair&lt;View, String&gt;(username, "username_tran");
        pairs[4] = new Pair&lt;View, String&gt;(password, "password_tran");
        pairs[5] = new Pair&lt;View, String&gt;(login_btn, "button_tran");
        pairs[6] = new Pair&lt;View, String&gt;(callSignUp, "login_signup_tran");

        //Call next activity by attaching the animation with it.
        if (android.os.Build.VERSION.SDK_INT &gt;= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        }
    }*/

}
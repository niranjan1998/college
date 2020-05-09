package project.msc.college;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class userLogin extends AppCompatActivity {

    Button isUser;
    EditText username, password;
    SharedPreferences sharedPreferences, userData;
    SharedPreferences.Editor editor, userEditor;
    Boolean saveLogin;
    CheckBox sl_checkbox;

    TextInputLayout v_username, v_password;

    ProgressBar progressBar;

    String userEnteredRoll, userEnteredPassword;

    private int password_count = 0;

    private long last_click = 0;

    @SuppressLint("CommitPrefEdits")
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

        sharedPreferences = getSharedPreferences("loginRef", MODE_PRIVATE);
        userData = getSharedPreferences("loginUser", MODE_PRIVATE);
        sl_checkbox = findViewById(R.id.checkbox);
        editor = sharedPreferences.edit();
        userEditor = userData.edit();

        isUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - last_click < 1000) {
                    return;
                }
                last_click = SystemClock.elapsedRealtime();
                loginUser();
            }
        });

        saveLogin = userData.getBoolean("saveLogin", true);
        if (saveLogin) {

            username.setText(userData.getString("roll", null));
            password.setText(userData.getString("password", null));

            if (!username.getText().toString().equals("")) {
                isUser();
            }
        }
    }

    public Boolean validateUsername() {
        String val = Objects.requireNonNull(v_username.getEditText()).getText().toString();

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
        String val = Objects.requireNonNull(v_password.getEditText()).getText().toString();

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

        } else {
            isUser();
        }

    }

    public void isUser() {
        userEnteredRoll = username.getText().toString().trim();
        userEnteredPassword = password.getText().toString().trim();

        if ((userEnteredRoll.equals("admin")) && (userEnteredPassword.equals("admin@890"))) {
            //save user data in shared preference
            if (sl_checkbox.isChecked()) {
                userEditor.putBoolean("saveLogin", true);
                userEditor.putString("roll", userEnteredRoll);
                userEditor.putString("password", userEnteredPassword);
                userEditor.apply();
            }
            Intent intent = new Intent(userLogin.this, admin_panel.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "admin here", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersData");

            Query checkUser = reference.orderByChild("roll").equalTo(userEnteredRoll);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        v_username.setError(null);
                        v_username.setErrorEnabled(false);

                        //  String token = FirebaseInstanceId.getInstance().getToken();

                        String passwordFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);

                        assert passwordFromDB != null;
                        if (passwordFromDB.equals(userEnteredPassword)) {

                            progressBar.setVisibility(View.VISIBLE);

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

                            //save user data in shared preference
                            if (sl_checkbox.isChecked()) {
                                userEditor.putBoolean("saveLogin", true);
                                userEditor.putString("roll", rollFromDB);
                                userEditor.putString("password", passFromDB);
                                userEditor.apply();
                            }
                            //store user information in sp
                            editor.putString("roll", rollFromDB);
                            editor.putString("name", nameFromDB);
                            editor.putString("email", emailFromDB);
                            editor.putString("class", streamFromDB);
                            editor.putString("phone", phoneNoFromDB);
                            editor.putString("password", passFromDB);
                            editor.putString("role", roleFromDB);
                            editor.apply();


                            //   userLoginHelper helper = new userLoginHelper();
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
                            finish();

                        } else {
                            password_count++;
                            v_password.setError("Wrong Password");
                            v_password.requestFocus();

                            if (password_count == 3) {

                                isUser.setEnabled(false);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        isUser.setEnabled(true);
                                    }
                                }, 30 * 1000);
                                password_count = 0;
                                Toast.makeText(userLogin.this, "hold " + password_count, Toast.LENGTH_SHORT).show();
                            }
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

    public void send_password(View view) {

        String val = Objects.requireNonNull(v_username.getEditText()).getText().toString();

        if (val.isEmpty()) {
            username.setError("Enter Roll Number");
        } else {
            userEnteredRoll = username.getText().toString().trim();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersData");

            Query checkUser = reference.orderByChild("roll").equalTo(userEnteredRoll);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        v_username.setError(null);
                        v_username.setErrorEnabled(false);

                        String passwordFromDB = dataSnapshot.child(userEnteredRoll).child("password").getValue(String.class);
                        String nameFromDB = dataSnapshot.child(userEnteredRoll).child("name").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredRoll).child("email").getValue(String.class);


                        final String mail_id = "niranjankusam25@gmail.com";
                        final String mail_pass = "kusam8090";

                        Properties properties = new Properties();
                        properties.setProperty("mail.transport.protocol", "smtp");
                        properties.put("mail.smtp.starttls.enable", "true");
                        properties.put("mail.smtp.auth", "true");
                        properties.put("mail.smtp.host", "smtp.gmail.com");
                        properties.put("mail.smtp.port", "587");
                        properties.put("mail.from", "niranjankusam25@gmail.com");

                        Session session = Session.getInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(mail_id, mail_pass);
                            }
                        });

                        try {
                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(mail_id));
                            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailFromDB));
                            message.setSubject("Requested Details");
                            String name = "Name : " + " " + nameFromDB;
                            String roll = "User Name : " + " " + userEnteredRoll;
                            String pass = "Password : " + " " + passwordFromDB;
                            String body = name + " " + roll + " " + pass;
                            message.setContent(body, "text/html; charset=UTF-8");
                            new SendMail().execute(message);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        v_username.setError("Invalid Username");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private class SendMail extends AsyncTask<Message, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(userLogin.this, "Please wait", "Sending mail", true);

        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if (s.equals("Success")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(userLogin.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color = '#509324'> Success</font>"));
                builder.setMessage("Mail Send Successfully");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(userLogin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
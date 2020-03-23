package com.example.college;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegUsers extends AppCompatActivity implements OnItemSelectedListener {

    EditText regName, regRoll, regPhone, regEmail;
    Button regBtn;

    String name, roll, phone, email, password, stream, role, pic;

    Spinner spinner;
    String item;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_users);

        regName = findViewById(R.id.reg_name);
        regRoll = findViewById(R.id.reg_roll);
        regEmail = findViewById(R.id.reg_email);
        regPhone = findViewById(R.id.reg_phone);
        regBtn = findViewById(R.id.btn_Register);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateField();
            }
        });

        //spinner element
        spinner = findViewById(R.id.spinner);
        //spinner click listener
        spinner.setOnItemSelectedListener(this);
        //spinner drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(0, "Select Stream");
        categories.add("MSc IT 1");
        categories.add("MSc IT 2");
        categories.add("BSc IT 1");
        categories.add("BSc IT 2");
        categories.add("BSc IT 3");

        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        //Drop Down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attach data adapter
        spinner.setAdapter(dataAdapter);
    }

    private void validateField() {

        name = regName.getText().toString();
        roll = regRoll.getText().toString();
        phone = regPhone.getText().toString();
        email = regEmail.getText().toString();
        password = regRoll.getText().toString();
        stream = item;
        if (name.trim().equals("")) {
            regName.setError("Enter Name");
        } else if (roll.trim().equals("")) {
            regRoll.setError("Enter Roll no");
        } else if (phone.trim().equals("")) {
            regPhone.setError("Enter Phone no");
        } else if (phone.trim().length() < 10) {
            regPhone.setError("Incorrect Phone no");
        } else if (email.trim().equals("")) {
            regEmail.setError("Enter e-mail");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regEmail.setError("Not a valid e-mail");
        } else if (item.trim().equals("Select Stream")) {
            Toast.makeText(getApplicationContext(), "Select Stream ", Toast.LENGTH_SHORT).show();
        } else {
            addStudent();
        }
    }

    private void addStudent() {
        String student = "Student";
        String profile =
                "https://firebasestorage.googleapis.com/v0/b/college-36833.appspot.com/o/images%20(6).jpeg?alt=media&token=f4825fa5-4a74-4698-82fd-06b17080ab98";
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("studentData");

        name = regName.getText().toString();
        roll = regRoll.getText().toString();
        phone = regPhone.getText().toString();
        email = regEmail.getText().toString();
        password = regRoll.getText().toString();
        stream = item;
        role = student;
        pic = profile;

        UserHelperClass helperClass = new UserHelperClass(name, roll, phone, email, password, stream, role, pic);

        reference.child(roll).setValue(helperClass);

        Toast.makeText(getApplicationContext(), "Student Added", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
        }

        //on selecting a spinner item
        item = parent.getItemAtPosition(position).toString();
        //showing selected
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        /*else {
            Toast.makeText(parent.getContext(), "Select Stream ", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
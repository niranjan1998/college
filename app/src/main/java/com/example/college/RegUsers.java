package com.example.college;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegUsers extends AppCompatActivity implements OnItemSelectedListener {

    TextInputLayout regName, regRoll, regPhone, regEmail, regPassword;
    Button regBtn;

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
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.btn_Register);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("studentData");

                String name = regName.getEditText().getText().toString();
                String roll = regRoll.getEditText().getText().toString();
                String phone = regPhone.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String stream = item;


                UserHelperClass helperClass = new UserHelperClass(name, roll, phone, email, password, stream);

                reference.child(roll).setValue(helperClass);


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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //on selecting a spinner item
        item = parent.getItemAtPosition(position).toString();
        //showing selected
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
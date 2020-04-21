package project.msc.college;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class reg_teacher extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
        setContentView(R.layout.activity_reg_teacher);

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
        categories.add("IT");
        categories.add("CS");

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
            checkUser();
        }
    }


    public void checkUser() {
        roll = regRoll.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersData");

        Query checkUser = reference.orderByChild("roll").equalTo(roll);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regRoll.setError("User exist");
                } else {
                    addTeacher();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addTeacher() {
        String teacher = "Teacher";
        String profile = "https://firebasestorage.googleapis.com/v0/b/college-36833.appspot.com/o/addprofile.jpeg?alt=media&token=eb2dde58-e796-491f-9183-13d8f469579c";
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("UsersData");

        name = regName.getText().toString();
        roll = regRoll.getText().toString();
        phone = regPhone.getText().toString();
        email = regEmail.getText().toString();
        password = regRoll.getText().toString();
        stream = item;
        role = teacher;
        pic = profile;

        UserHelperClass helperClass = new UserHelperClass(name, roll, phone, email, password, stream, role, pic);

        reference.child(roll).setValue(helperClass);

        Toast.makeText(getApplicationContext(), "Teacher Added", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();

        if (position != 0) {


            //on selecting a spinner item
            //   item = parent.getItemAtPosition(position).toString();
            //showing selected
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        }
        /*else {
            Toast.makeText(parent.getContext(), "Select Stream ", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

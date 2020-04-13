package com.example.college;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class todo_add extends AppCompatActivity {

    EditText ed_title, ed_desc;
    TextView user_name, class_name, txt_date;
    Button btn_upload;
    String s_title, s_desc, s_date, s_class;
    String timeTxt;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Add Class TODO");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ed_title = findViewById(R.id.ed_title);
        ed_desc = findViewById(R.id.ed_desc);
        txt_date = findViewById(R.id.txt_date);

        user_name = findViewById(R.id.user_name);
        class_name = findViewById(R.id.class_name);
        btn_upload = findViewById(R.id.btnUpload);


        Intent intent = getIntent();
        String className = intent.getStringExtra("grp_name");


        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String s_name = result.getString("name", "");

        user_name.setText(s_name);
        class_name.setText(className);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateField();
            }
        });
    }

    private void validateField() {


        s_title = ed_title.getText().toString();
        s_date = txt_date.getText().toString();
        s_desc = ed_desc.getText().toString();

        if (s_title.trim().equals("")) {
            ed_title.setError("Enter Title");
        } else if (s_desc.trim().equals("")) {
            ed_desc.setError("Field cannot be empty ");
        } else if (s_date.trim().equals("")) {
            txt_date.setError("Select Date and Time");
        } else {
            btnUploadTodo();
        }
    }

    private void btnUploadTodo() {

        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String s_name = result.getString("name", "");

        s_title = ed_title.getText().toString();
        s_date = txt_date.getText().toString();
        s_desc = ed_desc.getText().toString();
        s_name = user_name.getText().toString();
        s_class = class_name.getText().toString();


        String myCurrentDate = java.text.DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());


        reference = FirebaseDatabase.getInstance().getReference("todoData");

        String id = reference.push().getKey();
        todo_model todo_model = new todo_model(s_title, s_desc, s_date,myCurrentDate, id, s_name, s_class);

        assert id != null;
        reference.child(s_class).child(id).setValue(todo_model);

        Toast.makeText(getApplicationContext(), " Added", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, todo_main.class);
        startActivity(intent);
        finish();
    }
/*
    public void pick_time() {
        final Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);

                timeTxt = DateFormat.format("h:mm a", calendar1).toString();


            }
        }, HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }*/

    public void btn_pick_dateTime(View view) {

        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);

             //   pick_time();
                String dateTxt = DateFormat.format("MMM d,yyyy", calendar1).toString();
                txt_date.setText(dateTxt );

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i_intent = getIntent();
        String className = i_intent.getStringExtra("grp_name");

        Intent intent = new Intent(getApplicationContext(), todo_main.class);
        intent.putExtra("item_name", className);
        startActivity(intent);
        finish();
    }
}

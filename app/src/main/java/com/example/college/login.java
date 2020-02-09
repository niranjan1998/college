package com.example.college;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;

public class login extends AppCompatActivity implements OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //spinner click listener
        spinner.setOnItemSelectedListener(this);
        //spinner drop down elements
        List<String>categories = new ArrayList<String>();
        categories.add("MSc IT 1");
        categories.add("MSc IT 2");
        categories.add("BSc IT 1");
        categories.add("BSc IT 2");
        categories.add("BSc IT 3");

        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        //Drop Down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attach data adapter
        spinner.setAdapter(dataAdapter);
    }
    public void onItemSelected(AdapterView<?> parent,View view, int position,long id) {
        //on selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        //showing selected
        Toast.makeText(parent.getContext(), "Selected: " + item,Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?>arg0){

    }
}

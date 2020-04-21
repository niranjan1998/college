package project.msc.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class todo_main extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView txt_userName, txt_grpName;
    Spinner spinner;
    String item;
    RelativeLayout linearLayout;
    FloatingActionButton floatingActionButton;


    //for getting data
    RecyclerView recyclerView;
    List<todo_model> todoModelList;
    DatabaseReference dbReference;
    ValueEventListener todoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_main);


        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Class TODO");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txt_userName = findViewById(R.id.txt_name);
        txt_grpName = findViewById(R.id.txt_class);
        floatingActionButton = findViewById(R.id.fb_add);

        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String sp_name = result.getString("name", "");

        txt_userName.setText(sp_name);

        linearLayout = findViewById(R.id.lin_spinner);
        //to show comments
        recyclerView = findViewById(R.id.recycle_view);

        //spinner element
        spinner = findViewById(R.id.spinner);

        String sp_stream = result.getString("role", "");
        String sp_class = result.getString("class", "");

        if (sp_stream.trim().equals("Student")) {
            linearLayout.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            //   spinner.setVisibility(View.GONE);
            item = sp_class;
            txt_grpName.setText(item);
            showList();
        } else {
            txt_grpName.setVisibility(View.GONE);
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    /*    if (item.trim().equals("")) {

            Intent i_intent = getIntent();
            item = i_intent.getStringExtra("item_name");

        } else {}*/
        item = parent.getItemAtPosition(position).toString();

        showList();

        if (position != 0) {
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // recyclerView.setVisibility(View.GONE);
        try {
            Intent i_intent = getIntent();
            item = i_intent.getStringExtra("item_name");
            showList();
            Toast.makeText(getApplicationContext(), "trying ...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showList() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(todo_main.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        todoModelList = new ArrayList<>();
        final todo_adapter t_adapter = new todo_adapter(todo_main.this, todoModelList);
        recyclerView.setAdapter(t_adapter);

        dbReference = FirebaseDatabase.getInstance().getReference("todoData").child(item);

        todoListener = dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todoModelList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    todo_model t_model = itemSnapshot.getValue(todo_model.class);
                    todoModelList.add(t_model);
                }

                t_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void open_add(View view) {
        Intent intent = new Intent(getApplicationContext(), todo_add.class);
        intent.putExtra("grp_name", item);
        startActivity(intent);
    }

}

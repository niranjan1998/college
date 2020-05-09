package project.msc.college;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class todo_main extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView txt_userName, txt_grpName;
    Spinner spinner;
    String item;
    RelativeLayout linearLayout, upper_rl;
    FloatingActionButton floatingActionButton;
    MaterialButton view_all, view_today, view_tomorrow, view_notCompleted, view_completed;

    todo_adapter t_adapter;
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

        view_all = findViewById(R.id.view_all);
        view_today = findViewById(R.id.view_today);
        view_tomorrow = findViewById(R.id.view_tomorrow);
        view_completed = findViewById(R.id.view_completed);
        view_notCompleted = findViewById(R.id.view_notCompleted);

        view_all.performClick();

        SharedPreferences result = getSharedPreferences("loginRef", MODE_PRIVATE);
        String sp_name = result.getString("name", "");

        txt_userName.setText(sp_name);

        linearLayout = findViewById(R.id.lin_spinner);
        upper_rl = findViewById(R.id.rel_text);
        //to show comments
        recyclerView = findViewById(R.id.recycle_view);

        //spinner element
        spinner = findViewById(R.id.spinner);

        String sp_stream = result.getString("role", "");
        String sp_class = result.getString("class", "");

        if (sp_stream.trim().equals("Student")) {
            upper_rl.setVisibility(View.GONE);
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
     /*   // recyclerView.setVisibility(View.GONE);
        try {
            Intent i_intent = getIntent();
            item = i_intent.getStringExtra("item_name");
            showList();
            Toast.makeText(getApplicationContext(), "trying ...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void showList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(todo_main.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        todoModelList = new ArrayList<>();
        t_adapter = new todo_adapter(todo_main.this, todoModelList);
        recyclerView.setAdapter(t_adapter);

        dbReference = FirebaseDatabase.getInstance().getReference("todoData").child(item);

        todoListener = dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();
                //    Use SimpleDateFormat to format the Date as a String:
                DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy", Locale.getDefault());

                final String today_date = dateFormat.format(today);
                final String tomorrow_date = dateFormat.format(tomorrow);

                todoModelList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    todo_model t_model = itemSnapshot.getValue(todo_model.class);
                    todoModelList.add(t_model);

                    view_all.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    view_completed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    view_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    view_tomorrow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    view_notCompleted.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

                }
                t_adapter.notifyDataSetChanged();


                view_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoModelList.clear();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            todo_model t_model = itemSnapshot.getValue(todo_model.class);
                            todoModelList.add(t_model);
                            view_all.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                            view_completed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            view_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            view_tomorrow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            view_notCompleted.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

                        }
                        t_adapter.notifyDataSetChanged();
                    }
                });
                view_today.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoModelList.clear();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            todo_model t_model = itemSnapshot.getValue(todo_model.class);
                            assert t_model != null;
                            if (t_model.getDate().trim().equals(today_date)) {
                                todoModelList.add(t_model);
                                view_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                                view_completed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_all.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_tomorrow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_notCompleted.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            } else {
                                Toast.makeText(todo_main.this, "No Data Found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        t_adapter.notifyDataSetChanged();

                    }
                });
                view_tomorrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoModelList.clear();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            todo_model t_model = itemSnapshot.getValue(todo_model.class);
                            assert t_model != null;
                            if (t_model.getDate().trim().equals(tomorrow_date)) {
                                todoModelList.add(t_model);
                                view_tomorrow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                                view_completed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_all.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_notCompleted.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            } else {
                                Toast.makeText(todo_main.this, "No Data Found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        t_adapter.notifyDataSetChanged();
                    }
                });
                view_completed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoModelList.clear();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            todo_model t_model = itemSnapshot.getValue(todo_model.class);
                            assert t_model != null;
                            if (t_model.getStatus().trim().equals("Completed")) {
                                todoModelList.add(t_model);
                                view_completed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                                view_all.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_tomorrow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_notCompleted.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            } else {
                                Toast.makeText(todo_main.this, "No Data Found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        t_adapter.notifyDataSetChanged();
                    }
                });
                view_notCompleted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoModelList.clear();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            todo_model t_model = itemSnapshot.getValue(todo_model.class);
                            assert t_model != null;
                            if (t_model.getStatus().trim().equals("notCompleted")) {
                                todoModelList.add(t_model);
                                view_notCompleted.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                                view_completed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_tomorrow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                view_all.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            } else {
                                Toast.makeText(todo_main.this, "No Data Found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        t_adapter.notifyDataSetChanged();
                    }
                });

                if (t_adapter.getItemCount() == 0) {
                    Toast.makeText(todo_main.this, "Empty LIst", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        //this.showList();
        getMenuInflater().inflate(R.menu.search_toolbar, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint(Html.fromHtml("<font color = #FFFFFF> Type here to search</font>"));

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                t_adapter.getFilter().filter(newText);
                return true;
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }

    public void open_add(View view) {
        if (Arrays.asList("MSc IT 1", "MSc IT 2", "BSc IT 1", "BSc IT 2", "BSc IT 3").contains(item)) {
            Intent intent = new Intent(getApplicationContext(), todo_add.class);
            intent.putExtra("grp_name", item);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Select Class", Toast.LENGTH_SHORT).show();
        }

    }

}

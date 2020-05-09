package project.msc.college;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class admin_show_std extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialToolbar materialToolbar;
    MaterialTextView materialTextView;

    String i_role;
    Spinner spinner;
    String item;

    String Name, Roll, E_mail, Phone, Password;

    EditText ed_search;

    RecyclerView recyclerView;

    MaterialCardView materialCardView;

    List<UserHelperClass> adminUserList;
    admin_user_adapter admin_user_adapter;
    DatabaseReference databaseReference;
    ValueEventListener usersListener;

    FloatingActionButton fab_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_std);

        materialToolbar = findViewById(R.id.toll_bar);
        recyclerView = findViewById(R.id.recycle_view);
        ed_search = findViewById(R.id.ed_search);
        materialCardView = findViewById(R.id.card_empty);
        materialTextView = findViewById(R.id.txt_empty);

        Intent roles = getIntent();
        i_role = roles.getStringExtra("role");

        materialToolbar.setTitle(i_role + " Details");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fab_report = findViewById(R.id.fab_report);


        //spinner element
        spinner = findViewById(R.id.spinner);
        //spinner click listener
        spinner.setOnItemSelectedListener(this);
        //spinner drop down elements
        List<String> categories = new ArrayList<>();
        categories.add(0, "Select Stream");
        if (i_role.trim().equals("Student")) {
            categories.add("MSc IT 1");
            categories.add("MSc IT 2");
            categories.add("BSc IT 1");
            categories.add("BSc IT 2");
            categories.add("BSc IT 3");
        } else {
            categories.add("IT");
            categories.add("CS");
        }


        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        //Drop Down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attach data adapter
        spinner.setAdapter(dataAdapter);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();

        if (position != 0) {
            recyclerView.setVisibility(View.VISIBLE);
/*
            ed_search.setVisibility(View.VISIBLE);
*/
            showUsers();
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        }
        if (item.trim().equals("Select Stream")) {
            recyclerView.setVisibility(View.GONE);
            ed_search.setVisibility(View.GONE);
            Toast.makeText(parent.getContext(), "Select Stream ", Toast.LENGTH_SHORT).show();

        }
    }

    public void showUsers() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(admin_show_std.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adminUserList = new ArrayList<>();

        admin_user_adapter = new admin_user_adapter(admin_show_std.this, adminUserList);
        recyclerView.setAdapter(admin_user_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UsersData");
        /* usersListener = databaseReference.orderByChild("stream").equalTo(stream).addValueEventListener(new ValueEventListener() {
         */
        usersListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminUserList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    final UserHelperClass userHelperClass = itemSnapshot.getValue(UserHelperClass.class);

                    assert userHelperClass != null;
                    if (userHelperClass.getStream().trim().equals(item.trim())) {
                        adminUserList.add(userHelperClass);

                        fab_report.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //generate data
                                StringBuilder data = new StringBuilder();
                                data.append("Name,Roll,E-mail,Phone,Password");
                                for (int i = 0; i < adminUserList.size(); i++) {
                                    Name = String.valueOf(adminUserList.get(i).getName());
                                    Roll = String.valueOf(adminUserList.get(i).getRoll());
                                    E_mail = String.valueOf(adminUserList.get(i).getEmail());
                                    Phone = String.valueOf(adminUserList.get(i).getPhone());
                                    Password = String.valueOf(adminUserList.get(i).getPassword());

                                    data.append("\n").append(String.valueOf(Name))
                                            .append(",").append(String.valueOf(Roll))
                                            .append(",").append(String.valueOf(E_mail))
                                            .append(",").append(String.valueOf(Phone))
                                            .append(",").append(String.valueOf(Password));
                                }
                                try {

                                    String myCurrentDate = DateFormat.getDateTimeInstance()
                                            .format(Calendar.getInstance().getTime());

                                    String doc_name = item.trim() + " " + myCurrentDate;

                                    //saving the file into device
                                    FileOutputStream out = openFileOutput("data.csv", MODE_PRIVATE);
                                    out.write((data.toString()).getBytes());
                                    out.close();

                                    //exporting
                                    Context context = getApplicationContext();
                                    File fileLocation = new File(getFilesDir(), "data.csv");
                                    Uri path = FileProvider.getUriForFile(context, "project.msc.college", fileLocation);
                                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                                    fileIntent.setType("text/csv");
                                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, doc_name);
                                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
                                } catch (
                                        Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }


                if (admin_user_adapter.getItemCount() == 0) {
                    fab_report.setVisibility(View.INVISIBLE);
                    materialCardView.setVisibility(View.VISIBLE);
                    materialTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    materialTextView.setSelected(true);
                } else {
                    fab_report.setVisibility(View.VISIBLE);
                    materialCardView.setVisibility(View.GONE);
                }
                admin_user_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });
    }

    private void filter(String toString) {
        ArrayList<UserHelperClass> filterUsers = new ArrayList<>();
        for (UserHelperClass item : adminUserList) {
            if (item.getName().toLowerCase().contains(toString.toLowerCase()) || item.getRoll().toLowerCase().contains(toString.toLowerCase())) {
                filterUsers.add(item);
            }
        }
        admin_user_adapter.filteredList(filterUsers);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        recyclerView.setVisibility(View.GONE);
    }

    public void show_search(View view) {
        ed_search.setVisibility(View.VISIBLE);
    }
}

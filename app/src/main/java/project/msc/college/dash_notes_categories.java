package project.msc.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class dash_notes_categories extends AppCompatActivity {
    MaterialToolbar materialToolbar;
    TextView tv_main, tv_sem;

    String main, sem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_notes_categories);

        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Notes");
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tv_sem = findViewById(R.id.tv_sem);
        tv_main = findViewById(R.id.tv_main);

        Intent intent = getIntent();
        main = intent.getStringExtra("main");
        sem = intent.getStringExtra("sem");

        tv_main.setText(main);
        tv_sem.setText(sem);
    }

    public void card_notes(View view) {


        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("extra", "notes");
        intent.putExtra("main", main);
        intent.putExtra("sem", sem);
        startActivity(intent);
    }

    public void card_syllabus(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("extra", "syllabus");
        intent.putExtra("main", main);
        intent.putExtra("sem", sem);
        startActivity(intent);
    }

    public void card_practicals(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("extra", "practicals");
        intent.putExtra("main", main);
        intent.putExtra("sem", sem);
        startActivity(intent);
    }

    public void card_blackBook(View view) {

        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("extra", "blackBook");
        intent.putExtra("main", main);
        intent.putExtra("sem", sem);
        startActivity(intent);
    }
}

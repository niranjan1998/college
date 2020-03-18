package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class dash_notes extends AppCompatActivity {

    LinearLayout lin_sem, lin_bscSub, lin_mscSub;
    CardView msc_It, bsc_It, msc_sem1, msc_sem2, msc_sem3, msc_sem4, bsc_sem1, bsc_sem2, bsc_sem3, bsc_sem4, bsc_sem5, bsc_sem6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_notes);

        lin_sem = findViewById(R.id.lin_sem);
        lin_bscSub = findViewById(R.id.lin_bscSub);
        lin_mscSub = findViewById(R.id.lin_mscSub);
        msc_It = findViewById(R.id.cd_mscIt);
        bsc_It = findViewById(R.id.cd_bscIt);

        bsc_sem1 = findViewById(R.id.bsc_sem1);
        bsc_sem2 = findViewById(R.id.bsc_sem2);
        bsc_sem3 = findViewById(R.id.bsc_sem3);
        bsc_sem4 = findViewById(R.id.bsc_sem4);
        bsc_sem5 = findViewById(R.id.bsc_sem5);
        bsc_sem6 = findViewById(R.id.bsc_sem6);

        msc_sem1 = findViewById(R.id.msc_sem1);
        msc_sem2 = findViewById(R.id.msc_sem2);
        msc_sem3 = findViewById(R.id.msc_sem3);
        msc_sem4 = findViewById(R.id.msc_sem4);

    }

    public void mscIt(View view) {
        msc_It.setVisibility(View.GONE);
        bsc_It.setVisibility(View.GONE);
        lin_sem.setVisibility(View.GONE);
        lin_mscSub.setVisibility(View.VISIBLE);
        lin_bscSub.setVisibility(View.GONE);
    }

    public void bscIt(View view) {
        msc_It.setVisibility(View.GONE);
        bsc_It.setVisibility(View.GONE);
        lin_sem.setVisibility(View.GONE);
        lin_mscSub.setVisibility(View.GONE);
        lin_bscSub.setVisibility(View.VISIBLE);
    }


    public void bsc_notes1(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "BSC");
        intent.putExtra("sem", "SEMESTER-1");
        startActivity(intent);
    }

    public void bsc_notes2(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "BSC");
        intent.putExtra("sem", "SEMESTER-2");
        startActivity(intent);
    }

    public void bsc_notes3(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "BSC");
        intent.putExtra("sem", "SEMESTER-3");
        startActivity(intent);
    }

    public void bsc_notes4(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "BSC");
        intent.putExtra("sem", "SEMESTER-4");
        startActivity(intent);
    }

    public void bsc_notes5(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "BSC");
        intent.putExtra("sem", "SEMESTER-5");
        startActivity(intent);
    }

    public void bsc_notes6(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "BSC");
        intent.putExtra("sem", "SEMESTER-6");
        startActivity(intent);
    }

    public void msc_notes1(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "MSC");
        intent.putExtra("sem", "SEMESTER-1");
        startActivity(intent);
    }

    public void msc_notes2(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "MSC");
        intent.putExtra("sem", "SEMESTER-2");
        startActivity(intent);
    }

    public void msc_notes3(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "MSC");
        intent.putExtra("sem", "SEMESTER-3");
        startActivity(intent);
    }

    public void msc_notes4(View view) {
        Intent intent = new Intent(getApplicationContext(), dash_notes_list.class);
        intent.putExtra("main", "MSC");
        intent.putExtra("sem", "SEMESTER-4");
        startActivity(intent);
    }

}

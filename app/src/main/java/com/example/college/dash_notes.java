package com.example.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class dash_notes extends AppCompatActivity {

    LinearLayout lin_sem,lin_bscSub,lin_mscSub;
    CardView msc_It,bsc_It;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_notes);

        lin_sem = findViewById(R.id.lin_sem);
        lin_bscSub = findViewById(R.id.lin_bscSub);
        lin_mscSub = findViewById(R.id.lin_mscSub);
        msc_It = findViewById(R.id.cd_mscIt);
        bsc_It = findViewById(R.id.cd_bscIt);

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
        lin_bscSub.setVisibility(View.VISIBLE);    }

    public void mscIt_sem1(View view) {
        Intent intent = new  Intent(dash_notes.this,viewfiles.class);
        startActivity(intent);
    }
}

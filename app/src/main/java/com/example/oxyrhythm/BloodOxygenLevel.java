package com.example.oxyrhythm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BloodOxygenLevel extends Dashboard {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_oxygen_level);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
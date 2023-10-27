package com.example.oxyrhythm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume(){
        super.onResume();
        newUser();
    }

    private void newUser() {
        Intent intent = new Intent(this, GetStartedActivity.class);
        startActivity(intent);
    }
}
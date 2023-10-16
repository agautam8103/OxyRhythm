package com.example.oxyrhythm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        Button get_started_btn = findViewById(R.id.new_usr_get_started_btn);
        get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {registerUser();}});
    }

    private void registerUser() {
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }
}
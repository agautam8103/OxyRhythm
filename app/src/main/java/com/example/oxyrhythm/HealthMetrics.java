package com.example.oxyrhythm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HealthMetrics extends AppCompatActivity {

    private OxyUser oxy_user;
    private DataBase oxy_user_saved_data;
    TextView height_val, weight_val;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthmetrics);

        oxy_user_saved_data = new DataBase(HealthMetrics.this);
        height_val = findViewById(R.id.Height_val);
        weight_val = findViewById(R.id.Weight_val);
    }

    @Override
    protected void onResume() {
        super.onResume();

        oxy_user = oxy_user_saved_data.getSavedOxyUser();

        height_val.setText("Height: " + oxy_user.getHeight());
        weight_val.setText("Weight: " + oxy_user.getWeight());
    }
}

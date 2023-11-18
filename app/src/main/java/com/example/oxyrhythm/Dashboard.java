package com.example.oxyrhythm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Dashboard extends AppCompatActivity {

    private DataBase oxy_user_saved_data;
    TextView greeting_oxy_user, dash_mesg, heart_rate_label, blood_oxygen_level_label, body_temperature_label,
            Click_to_see_more_label1, Click_to_see_more_label2, Click_to_see_more_label3;
    Button heart_rate_BTN, blood_oxy_BTN, body_temp_BTN, health_metics_BTN, help_BTN;
    ImageView heart_pic, temp_pic, blood_oxy_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        heart_rate_BTN = findViewById(R.id.heart_rate_btn);
        heart_rate_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {heartRate();}});
        
        blood_oxy_BTN = findViewById(R.id.blood_oxygen_btn);
        blood_oxy_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {bloodOxygenLevel();}});
        
        body_temp_BTN = findViewById(R.id.body_temperature_btn);
        body_temp_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {temperature();}});
        
        health_metics_BTN = findViewById(R.id.health_metric_btn);
        health_metics_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {healthMetrics();}});
        
        help_BTN = findViewById(R.id.help_btn);
        help_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {help();}});
        
        oxy_user_saved_data = new DataBase(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.hello_OxyUser_label);
        dash_mesg = findViewById(R.id.dash_mesg);
        heart_rate_label = findViewById(R.id.heart_rate_label);
        blood_oxygen_level_label = findViewById(R.id.blood_oxygen_level_label);
        body_temperature_label = findViewById(R.id.body_temperature_label);
        Click_to_see_more_label1 = findViewById(R.id.Click_to_see_more_label1);
        Click_to_see_more_label2 = findViewById(R.id.Click_to_see_more_label2);
        Click_to_see_more_label3 = findViewById(R.id.Click_to_see_more_label3);

        heart_pic = findViewById(R.id.Heart_Pic);
        temp_pic = findViewById(R.id.Temp_pic);
        blood_oxy_pic = findViewById(R.id.blood_oxygen_pic);
    }

    @Override
    protected void onResume() {
        super.onResume();

        OxyUser oxy_user = oxy_user_saved_data.getSavedOxyUser();

        if (oxy_user.OxyUserIsEmpty()) {
            heart_rate_BTN.setVisibility(View.INVISIBLE);
            blood_oxy_BTN.setVisibility(View.INVISIBLE);
            body_temp_BTN.setVisibility(View.INVISIBLE);
            health_metics_BTN.setVisibility(View.INVISIBLE);
            help_BTN.setVisibility(View.INVISIBLE);

            heart_pic.setVisibility(View.INVISIBLE);
            temp_pic.setVisibility(View.INVISIBLE);

            Intent i = new Intent(this, GetStartedActivity.class);
            startActivity(i);
        } else {
            greeting_oxy_user.setText("Hello, " + oxy_user.getFirstName() + "!");
            dash_mesg.setText("Time to check up on your health routine");
            heart_rate_label.setText("Heart Rate");
            blood_oxygen_level_label.setText("Blood Oxygen Level");
            body_temperature_label.setText("Body Temperature");
            Click_to_see_more_label1.setText("Click for more info.");
            Click_to_see_more_label2.setText("Click for more info.");
            Click_to_see_more_label3.setText("Click for more info.");

            heart_rate_BTN.setVisibility(View.VISIBLE);
            blood_oxy_BTN.setVisibility(View.VISIBLE);
            body_temp_BTN.setVisibility(View.VISIBLE);
            health_metics_BTN.setVisibility(View.VISIBLE);
            help_BTN.setVisibility(View.VISIBLE);

            heart_pic.setVisibility(View.VISIBLE);
            temp_pic.setVisibility(View.VISIBLE);
            blood_oxy_pic.setVisibility(View.VISIBLE);
        }
    }

    private void heartRate() {
        Intent i = new Intent(this, HeartRate.class);
        startActivity(i);
    }

    private void bloodOxygenLevel() {
        Intent i = new Intent(this, BloodOxygenLevel.class);
        startActivity(i);
    }

    private void temperature() {
        Intent i = new Intent(this, Temperature.class);
        startActivity(i);
    }

    private void healthMetrics() {
        Intent i = new Intent(this, HealthMetrics.class);
        startActivity(i);
    }

    private void help() {
        Intent i = new Intent(this, Help.class);
        startActivity(i);
    }
}
package com.example.oxyrhythm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HealthMetrics extends AppCompatActivity {

    TextView MaxHeartRateText;
    TextView range85;
    TextView range65;
    TextView range45;
    TextView currentWeightText;
    TextView currentHeightText;
    TextView BMIText;
    TextView BMIindication;
    TextView MaxHeartRateText2;
    TextView range85_2;
    TextView range65_2;
    TextView range45_2;
    TextView currentWeightText2;
    TextView currentHeightText2;
    TextView BMIText2;
    TextView BMIindication2;
    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthmetrics);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataBase dataBase = new DataBase(this);
        OxyUser user = dataBase.getSavedOxyUser();

        MaxHeartRateText = findViewById(R.id.MaxHeartRateText);
        range85 = findViewById(R.id.range85);
        range65 = findViewById(R.id.range65);
        range45 = findViewById(R.id.range45);
        currentWeightText = findViewById(R.id.currentWeightText);
        currentHeightText = findViewById(R.id.currentHeightText);
        BMIText = findViewById(R.id.BMIText);
        BMIindication = findViewById(R.id.BMIindication);

        MaxHeartRateText2 =findViewById(R.id.MaxHeartRateText2);
        range85_2 = findViewById(R.id.range85_2);
        range65_2 = findViewById(R.id.range65_2);
        range45_2 = findViewById(R.id.range45_2);
        currentWeightText2 = findViewById(R.id.currentWeightText2);
        currentHeightText2 =findViewById(R.id.currentHeightText2);
        BMIText2 = findViewById(R.id.BMIText2);
        BMIindication2 = findViewById(R.id.BMIindication2);





        //floating inforfmation
        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        floatingActionButton2 = findViewById(R.id.floatingActionButton2);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2();
            }
        });



        //calculate and display max heart rate
        int by = user.getBirthYear();
        int age = 2023 - by;
        int max_heartrate = 220 - age;
        MaxHeartRateText2.setText(max_heartrate + " bpm");

        //calculate and display range 85%
        int hr_50p = (int) ((max_heartrate)*(0.5));
        int hr_60p = (int) ((max_heartrate)*(0.6));
        range85_2.setText( hr_50p + "-" + hr_60p + " bpm");

        //calculate and display range 65%
        int hr_70p = (int) ((max_heartrate)*(0.6));
        range65_2.setText( hr_60p + "-" + hr_70p + " bpm");

        //calculate and display range 45%
        int hr_80p = (int) ((max_heartrate)*(0.6));
        range45_2.setText(hr_70p + "-" + hr_80p + " bpm");

        //display weight
        float weightf=user.getWeight();
        currentWeightText2.setText( weightf + " kg");


        //display height
        float heightf=user.getHeight();
        currentHeightText2.setText(heightf + " cm");

        //calculate and display BMI
        float bmikg = weightf;
        float bmim = (heightf/100)*(heightf/100);
        float bmi = bmikg/bmim;
        BMIText2.setText(String.valueOf(bmi));

        //display BMI
        String unitWeight = user.getWeightUnit();
        String bmiInd;
        if(unitWeight == "lbs"){
            //weightf = (weightf/2.205);
        }
        if(bmi<18.5){
            bmiInd = "Underweight";
        }
        else if (bmi >= 18.5 && bmi <= 24.9) {
            bmiInd = "Normal Weight";
        }
        else if (bmi >= 25.0 && bmi <= 29.9) {
            bmiInd = "Overweight";
        }
        else {
            bmiInd = "Obese";
        }
        BMIindication2.setText(bmiInd);
    }


    public void openDialog(){
        HeartrateDialogFragment example = new HeartrateDialogFragment();
        example.show(getSupportFragmentManager(),"example dialog");
    }

    public void openDialog2(){
        BMIDialogFragment example = new BMIDialogFragment();
        example.show(getSupportFragmentManager(),"example dialog2");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufordashboard, menu);
        getMenuInflater().inflate(R.menu.health_metrics_menu, menu);
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menufordashboard) {
            Intent i = new Intent(this, RegisterUserActivity.class);
            startActivity(i);
            return true;
        }

        else if (item.getItemId() == R.id.health_metric) {
            Intent i = new Intent(this, HealthMetrics.class);
            startActivity(i);
            return true;
        }

        else if (item.getItemId() == R.id.help) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
            return true;
        }

        else return super.onOptionsItemSelected(item);
    }

}

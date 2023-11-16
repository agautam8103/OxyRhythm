package com.example.oxyrhythm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HeartRate extends AppCompatActivity {

    public TextView textView;
    public int heartrate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartmonitor);

        SharedPreferences sharedPreferences = getSharedPreferences(Dashboard.SHARED_PREF, MODE_PRIVATE);
        heartrate = sharedPreferences.getInt(Dashboard.key_data, 0);

        textView = findViewById(R.id.txtView);
//        updateText(heartrate);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(Integer.toString(heartrate));
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public void updateText(int data){
     runOnUiThread(new Runnable() {
         @Override
         public void run() {
             textView.setText(Integer.toString(data));
         }
     });
    }
}
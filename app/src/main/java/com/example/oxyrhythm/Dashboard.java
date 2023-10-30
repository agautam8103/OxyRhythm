package com.example.oxyrhythm;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {

    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView Intro = (TextView) findViewById(R.id.dashintro);
        dataBase = new DataBase(Dashboard.this);
        Intro.setText("Hello " + dataBase.getFirstName() + "!");
        Button heart = (Button) findViewById(R.id.checkheartrate);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartmonitor();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufordashboard, menu);
        return true;
    }

    public void heartmonitor(){
        Intent i = new Intent(this,HeartRate.class);
        startActivity(i);
    }

}

package com.example.oxyrhythm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        EditText editText = (EditText) findViewById(R.id.f_name_input);
        Button next = findViewById(R.id.save_usr_data_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dataBase = new DataBase(RegisterUserActivity.this);
            dataBase.saveFirstName(editText.getText().toString());
            dashboard();
            }

        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Spinner sex_choice = findViewById(R.id.sex_input);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sex_Options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_choice.setAdapter(adapter);
        sex_choice.setOnItemSelectedListener(this);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    public void dashboard(){
        Intent i = new Intent(this, Dashboard.class );
        startActivity(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
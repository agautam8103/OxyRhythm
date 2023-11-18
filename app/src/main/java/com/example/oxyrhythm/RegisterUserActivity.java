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
import android.widget.Toast;

public class RegisterUserActivity extends AppCompatActivity{

    private EditText first_name, last_name, birth_year, height, weight;
    private String sex;
    private DataBase save_oxy_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save_oxy_user = new DataBase(RegisterUserActivity.this);

        Button next = findViewById(R.id.save_usr_data_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {saveOxyUser();}
        });

        first_name = findViewById(R.id.f_name_input);
        last_name = findViewById(R.id.l_name_input);
        birth_year = findViewById(R.id.birth_year_input);
        height = findViewById(R.id.height_input);
        weight = findViewById(R.id.weight_input);

        Spinner sex_choice = findViewById(R.id.sex_input);
        ArrayAdapter<CharSequence> sex_adapter = ArrayAdapter.createFromResource(this, R.array.Sex_Options, android.R.layout.simple_spinner_item);
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_choice.setAdapter(sex_adapter);
        sex_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner height_units = findViewById(R.id.height_units);
        ArrayAdapter<CharSequence> height_units_adapter = ArrayAdapter.createFromResource(this, R.array.Height_Units, android.R.layout.simple_spinner_item);
        height_units_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_units.setAdapter(height_units_adapter);
        height_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner weight_units = findViewById(R.id.weight_units);
        ArrayAdapter<CharSequence> weight_units_adapter = ArrayAdapter.createFromResource(this, R.array.Weight_Units, android.R.layout.simple_spinner_item);
        weight_units_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weight_units.setAdapter(weight_units_adapter);
        weight_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        OxyUser user = save_oxy_user.getSavedOxyUser();

        if (user.OxyUserIsEmpty()) {
            EnableEdit(true);
        }

        else {
            EnableEdit(false);

            first_name.setText(user.getFirstName());
            last_name.setText(user.getLastName());
            birth_year.setText(Integer.toString(user.getBirthYear()));
            height.setText(Float.toString(user.getHeight()));
            weight.setText(Float.toString(user.getWeight()));
        }
    }

    private void EnableEdit(boolean enable) {
        first_name.setEnabled(enable);
        last_name.setEnabled(enable);
        birth_year.setEnabled(enable);
        height.setEnabled(enable);
        weight.setEnabled(enable);
    }

    private void saveOxyUser() {
        if (first_name.getText().length() == 0 || last_name.getText().length() == 0 || birth_year.getText().length() == 0 ||
                height.getText().length() == 0 || weight.getText().length() == 0) {
            Toast empty_message = Toast.makeText(getApplicationContext(), "No Field May Be Empty", Toast.LENGTH_LONG);
            empty_message.show();
        }

        else if (Integer.parseInt(birth_year.getText().toString()) < 1900 || Integer.parseInt(birth_year.getText().toString()) > 2023) {
            Toast invalid_birth_year_message = Toast.makeText(getApplicationContext(), "Enter birth year between 1900 and 2023", Toast.LENGTH_LONG);
            invalid_birth_year_message.show();
        }

        else if ((2023 - Integer.parseInt(birth_year.getText().toString())) < 18) {
            Toast under_age_message = Toast.makeText(getApplicationContext(), "You are under 18 years old. OxyRhythm is not for you", Toast.LENGTH_LONG);
            under_age_message.show();
        }

        else {
            save_oxy_user.saveFirstName(first_name.getText().toString());
            save_oxy_user.saveLastName(last_name.getText().toString());
            save_oxy_user.saveBirthYear(Integer.parseInt(birth_year.getText().toString()));
            save_oxy_user.saveWeight(Float.parseFloat(weight.getText().toString()));
            save_oxy_user.saveHeight(Float.parseFloat(height.getText().toString()));
            save_oxy_user.saveSex(sex);

            Intent i = new Intent(this, Dashboard.class);
            startActivity(i);
        }
    }
}
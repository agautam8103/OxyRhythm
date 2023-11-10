package com.example.oxyrhythm;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Dashboard extends AppCompatActivity {

    private OxyUser oxy_user;
    private DataBase oxy_user_saved_data;
    TextView greeting_oxy_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        oxy_user_saved_data = new DataBase(Dashboard.this);

        greeting_oxy_user = findViewById(R.id.hello_OxyUser_label);

        Button heart = findViewById(R.id.checkheartrate);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {HeartMonitor();}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        oxy_user = oxy_user_saved_data.getSavedOxyUser();

        if (oxy_user.OxyUserIsEmpty()) {
            Intent i = new Intent(this, GetStartedActivity.class);
            startActivity(i);
        }

        else {
            greeting_oxy_user.setText("Hello " + oxy_user.getFirstName() + "!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufordashboard, menu);
        return true;
    }

    public void HeartMonitor(){
        Intent i = new Intent(this,HeartRate.class);
        startActivity(i);
    }
}
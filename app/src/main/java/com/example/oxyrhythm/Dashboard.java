package com.example.oxyrhythm;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private OxyUser oxy_user;
    private DataBase oxy_user_saved_data;
    TextView greeting_oxy_user;
    LinearLayout heartrateWidget, healthmetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        LineChart lineChart = findViewById(R.id.lineChart);
        LineChart lineChart2 = findViewById(R.id.lineChart2);

        heartrateWidget = findViewById(R.id.heartRateWid);
        healthmetrics = findViewById(R.id.healthmetricsclick);
        healthmetrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    HealthMetrics();
            }
        });
        heartrateWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeartMonitor();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        float[] dataforheartrate = {74, 65, 55, 88, 100, 66, 78, 79, 82};
        float[] dataforbloodoxygen = {100, 99, 95, 88, 85, 100};

        setLineChartData(lineChart, dataforheartrate, "#FF0000");
        setLineChartData(lineChart2, dataforbloodoxygen, "#0000FF");

        oxy_user_saved_data = new DataBase(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.hello_OxyUser_label);
    }

    private void setLineChartData(LineChart chart, float[] data, String lineColor) {
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            entries.add(new Entry(i, data[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(Color.parseColor(lineColor));
        dataSet.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        chart.setData(new LineData(dataSets));
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);

        chart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        oxy_user = oxy_user_saved_data.getSavedOxyUser();

        if (oxy_user.OxyUserIsEmpty()) {
            Intent i = new Intent(this, GetStartedActivity.class);
             startActivity(i);
        } else {
            greeting_oxy_user.setText("Hello " + oxy_user.getFirstName() + "!");
        }
    }

    public void HeartMonitor() {
         Intent i = new Intent(this, HeartRate.class);
        startActivity(i);
    }

    public void HealthMetrics() {
        Intent i = new Intent(this, HealthMetrics.class);
        startActivity(i);
    }

}

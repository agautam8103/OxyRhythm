package com.example.oxyrhythm;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BloodOxygenLevel extends Dashboard {
    private TextView spO2healthstatus;
    private HW_Data healthData;

    private final Handler handler = new Handler();
    private static final int UPDATE_INTERVAL = 3000; // Update every 3000 milliseconds (3 second)

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateHealthStatus();
            handler.postDelayed(this, UPDATE_INTERVAL); // Schedule the next update
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_oxygen_level);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spO2healthstatus = (TextView) findViewById(R.id.spO2healthstatus);
        // Get the LineChart reference from your layout
        LineChart lineChart = findViewById(R.id.spO2chart);

        // Create a method to set up your LineChart and populate it with data
        setupLineChart(lineChart);

        // Initialize with default values
        healthData = new HW_Data(0, 0, 0);

        // Create a method to set up your LineChart and populate it with data
        setupLineChart(lineChart);

        // Start the continuous update loop
        handler.postDelayed(updateRunnable, UPDATE_INTERVAL);
    }

    private void setupLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 96));
        entries.add(new Entry(2, 97));
        entries.add(new Entry(3, 97));
        entries.add(new Entry(4, 96));
        entries.add(new Entry(5, 100));
        entries.add(new Entry(6, 97));


        LineDataSet dataSet = new LineDataSet(entries, "SpO2 Levels");


        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);


        LineData data = new LineData(dataSet);


        lineChart.setData(data);
        lineChart.getDescription().setText("Blood Oxygen Monitoring");  // Set description label
        lineChart.animateY(2000);  // Add animation

        // Customize the X and Y axes
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Time 1", "Time 2", "Time"}));
        xAxis.setDrawGridLines(false); // Disable vertical grid lines

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(6, true); // Set the number of labels on the Y-axis
        leftAxis.setDrawGridLines(false); // Disable horizontal grid lines

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);  // Disable the right Y axis

        // Customize the legend
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        // Refresh the chart to display the changes
        lineChart.invalidate();


    }

    // Update health status based on spO2 data
    private void updateHealthStatus() {
        String healthStatus;
        int currentSpO2 = healthData.getBloodOxygen();

        if (currentSpO2 < 90) {
            healthStatus = "Low";
        }
        else if (currentSpO2 >= 90 && currentSpO2 <=100) {
            healthStatus = "Normal";
        }
        else {
            healthStatus = "Normal";
        }
        // Update healthstatus TextView
        spO2healthstatus.setText(healthStatus);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the update Runnable when the activity is destroyed to avoid memory leaks
        handler.removeCallbacks(updateRunnable);
    }



}
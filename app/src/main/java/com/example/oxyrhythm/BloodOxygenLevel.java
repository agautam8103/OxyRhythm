package com.example.oxyrhythm;

import android.database.Cursor;
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

        // Initialize with default values
        healthData = new HW_Data(65, 96, 0);

        // Create a method to set up your LineChart and populate it with data
        setupLineChart(lineChart);

        // Start the continuous update loop
        handler.postDelayed(updateRunnable, UPDATE_INTERVAL);

        // Initialize the database
        database = new HW_Database_SQLite(BloodOxygenLevel.this);

        // Retrieve and display the last 5 values
        displayLastFiveValues();

    }

    private void displayLastFiveValues() {
        Cursor cursor = database.readAllData();

        if (cursor != null && cursor.moveToLast()) {
            ArrayList<Entry> entries = new ArrayList<>();
            int totalBlood = 0;
            int entryCount = 0;

            int bloodIndex = cursor.getColumnIndexOrThrow(HW_Database_SQLite.COLUMN_BLOOD_OXYGEN);

            int index = 0;
            do {
                int temp = cursor.getInt(bloodIndex);
                totalBlood += temp;
                entries.add(new Entry(index++, temp));
                entryCount++;
            } while (cursor.moveToPrevious() && entries.size() < 5); // Limit to 5 entries

            // Calculate average spo2
            int averageSpo2 = (entryCount >= 0) ? totalBlood / entryCount : 0;
            String healthStatus;
            int currentSpO2 = healthData.getBloodOxygen();

            if (averageSpo2  < 90) {
                healthStatus = "Low";
            }
            else  {
                healthStatus = "Normal";
            }
            // Update healthstatus TextView
            spO2healthstatus.setText(healthStatus);

            // Update the LineChart with the entries
            updateLineChart(entries);

            // Update the avgbpm TextView with the calculated average BPM
            TextView avgTempTextView = findViewById(R.id.avgspo2);
            avgTempTextView.setText("Avg Spo2: " + averageSpo2 + " %");

            // Calculate and update resting heart rate
            int restingSpo2 = calculateRestingSpo2(entries);
            TextView restingblood = findViewById(R.id.restingspo2rate);
            restingblood.setText("Resting Spo2: " + restingSpo2 + " %");
        } else {
            spO2healthstatus.setText("No data available");
        }

        // Close the cursor to avoid memory leaks
        if (cursor != null) {
            cursor.close();
        }
    }

    private int calculateRestingSpo2(ArrayList<Entry> entries) {
        // For example, you can use the last entry as the resting heart rate
        if (!entries.isEmpty()) {
            Entry lastEntry = entries.get(entries.size() - 1);
            return (int) lastEntry.getY();
        }
        return 0;
    }

    // New method to update LineChart with dynamic entries
    private void updateLineChart(ArrayList<Entry> entries) {
        LineChart lineChart = findViewById(R.id.spO2chart);

        LineDataSet dataSet = new LineDataSet(entries, "Blood Oxygen");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        LineData data = new LineData(dataSet);

        lineChart.setData(data);
        lineChart.getDescription().setText("Blood Oxygen Monitoring");
        lineChart.animateY(2000);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getTimeLabels(entries.size())));
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(6, true);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        lineChart.invalidate();
    }

    // New method to generate time labels based on entry count
    private String[] getTimeLabels(int entryCount) {
        String[] labels = new String[entryCount];
        for (int i = 0; i < entryCount; i++) {
            labels[i] = "Time " + (i + 1);
        }
        return labels;
    }

    private void setupLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 0));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 0));
        entries.add(new Entry(5, 0));
        entries.add(new Entry(6, 0));


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
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Time 1", "Time 2", "Time 3"}));
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
//        String healthStatus;
//        int currentSpO2 = healthData.getBloodOxygen();
//
//        if (currentSpO2 < 90) {
//            healthStatus = "Low";
//        }
//        else  {
//            healthStatus = "Normal";
//        }
//        // Update healthstatus TextView
//        spO2healthstatus.setText(healthStatus);
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
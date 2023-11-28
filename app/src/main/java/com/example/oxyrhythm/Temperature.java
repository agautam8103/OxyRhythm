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

public class Temperature extends Dashboard {

    private HW_Database_SQLite dbHelper;
    private HW_Data healthData;

    private final Handler handler = new Handler();
    private static final int UPDATE_INTERVAL = 3000; // Update every 3000 milliseconds (3 second)

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(this, UPDATE_INTERVAL); // Schedule the next update
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the LineChart reference from your layout
        LineChart lineChart = findViewById(R.id.tempchart);

        // Initialize with default values
        healthData = new HW_Data(65, 96, 0);

        // Create a method to set up your LineChart and populate it with data
        setupLineChart(lineChart);

        // Start the continuous update loop
        handler.postDelayed(updateRunnable, UPDATE_INTERVAL);

        //Initialize the database
        dbHelper = new HW_Database_SQLite(this);

        // Retrieve and display the last 5 values
        displayLastFiveValues();


    }

    private void displayLastFiveValues() {
        Cursor cursor = dbHelper.readAllData();

        if (cursor != null && cursor.moveToLast()) {
            ArrayList<Entry> entries = new ArrayList<>();
            int totalTemp = 0;
            int entryCount = 0;

            int tempIndex = cursor.getColumnIndexOrThrow(HW_Database_SQLite.COLUMN_TEMPERATURE);

            int index = 0;
            do {
                int temp = cursor.getInt(tempIndex);
                totalTemp += temp;
                entries.add(new Entry(index++, temp));
                entryCount++;
            } while (cursor.moveToPrevious() && entries.size() < 5); // Limit to 5 entries

            // Calculate average BPM
            int averageTemp = (entryCount >= 0) ? totalTemp / entryCount : 0;

            // Update the LineChart with the entries
            updateLineChart(entries);

            // Update the avgbpm TextView with the calculated average BPM
            TextView avgTempTextView = findViewById(R.id.avgtemp);
            avgTempTextView.setText("Avg Temp: " + averageTemp + " 'C");

            // Calculate and update resting heart rate
            int restingTemp = calculateRestingTemp(entries);
            TextView restingTempBody = findViewById(R.id.restingbody);
            restingTempBody.setText("Resting Temp: " + restingTemp + " 'C");
        } else {
            //hearthealthstatus.setText("No data available");
        }

        // Close the cursor to avoid memory leaks
        if (cursor != null) {
            cursor.close();
        }
    }

    private int calculateRestingTemp(ArrayList<Entry> entries) {
        // For example, you can use the last entry as the resting heart rate
        if (!entries.isEmpty()) {
            Entry lastEntry = entries.get(entries.size() - 1);
            return (int) lastEntry.getY();
        }
        return 0;
    }

    // New method to update LineChart with dynamic entries
    private void updateLineChart(ArrayList<Entry> entries) {
        LineChart lineChart = findViewById(R.id.tempchart);

        LineDataSet dataSet = new LineDataSet(entries, "Temperature");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        LineData data = new LineData(dataSet);

        lineChart.setData(data);
        lineChart.getDescription().setText("Temperature Monitoring");
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

    // New method to calculate resting heart rate
    private void setupLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 0));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 0));
        entries.add(new Entry(5, 0));
        entries.add(new Entry(6, 0));

        LineDataSet dataSet = new LineDataSet(entries, "Temperature");

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        LineData data = new LineData(dataSet);

        lineChart.setData(data);
        lineChart.getDescription().setText("Temperature Monitoring");  // Set description label
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

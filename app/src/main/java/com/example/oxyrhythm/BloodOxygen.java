package com.example.oxyrhythm;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BloodOxygen extends AppCompatActivity {

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodoxygenmonitor); // Make sure this is pointing to your correct XML file

        chart = findViewById(R.id.chart);
        setupChart();
        addMockDataToChart();
    }


    private void setupChart() {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setLabelRotationAngle(45f);
        xAxis.setGranularity(1f);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularity(1f); // Set the granularity of the Y-axis
        yAxisLeft.setAxisMinimum(90f); // Set the minimum value for the Y-axis
        yAxisLeft.setAxisMaximum(100f); // Set the maximum value for the Y-axis

        // Hide the Y-axis on the right side
        chart.getAxisRight().setEnabled(false);
    }

    private void addMockDataToChart() {
        ArrayList<Entry> entries = new ArrayList<>();

        // Generate mock data for 10 data points
        for (int i = 0; i < 10; i++) {
            long timestampInMillis = System.currentTimeMillis() + i * 1000; // Incrementing timestamps
            float spo2Value = (float) (Math.random() * 10) + 90; // Random SpO2 values between 90 and 100

            entries.add(new Entry(timestampInMillis, spo2Value));
        }

        LineDataSet dataSet = new LineDataSet(entries, "SpO2 Percentage");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        // Refresh the chart
        chart.invalidate();
    }

    private class MyXAxisValueFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Convert timestamp to a formatted date string
            return dateFormat.format(new Date((long) value));
        }
    }
}

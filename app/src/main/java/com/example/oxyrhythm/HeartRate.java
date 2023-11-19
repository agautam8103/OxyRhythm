package com.example.oxyrhythm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

public class HeartRate extends Dashboard {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartmonitor);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the LineChart reference from your layout
        LineChart lineChart = findViewById(R.id.heartchart);

        // Create a method to set up your LineChart and populate it with data
        setupLineChart(lineChart);
    }

    private void setupLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 60));
        entries.add(new Entry(2, 65));
        entries.add(new Entry(3, 70));
        entries.add(new Entry(4, 87));
        entries.add(new Entry(5, 77));
        entries.add(new Entry(6, 97));


        LineDataSet dataSet = new LineDataSet(entries, "Heart Rate");


        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);


        LineData data = new LineData(dataSet);


        lineChart.setData(data);
        lineChart.getDescription().setText("Heart Rate Monitoring");  // Set description label
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

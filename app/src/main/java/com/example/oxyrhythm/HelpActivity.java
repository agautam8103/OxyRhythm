package com.example.oxyrhythm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.oxyrhythm.HelpSection.FAQAdapter;
import com.example.oxyrhythm.HelpSection.FAQItem;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sample FAQs data
        List<FAQItem> faqList = new ArrayList<>();
        faqList.add(new FAQItem("How to measure heart rate?", "To measure heart rate, follow these steps..."));
        faqList.add(new FAQItem("Troubleshooting connection issues with sensors", "If you encounter connection issues..."));
        // Add more FAQs as needed
        RecyclerView faqRecyclerView = findViewById(R.id.recyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        faqRecyclerView.setAdapter(faqAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    
}
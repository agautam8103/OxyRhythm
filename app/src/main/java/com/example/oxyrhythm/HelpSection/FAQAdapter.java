package com.example.oxyrhythm.HelpSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oxyrhythm.R;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder>
{
    private List<FAQItem> faqList;
    public FAQAdapter(List<FAQItem> faqList) {
        this.faqList = faqList;
    }
    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq,
                parent, false);
        return new FAQViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQItem faqItem = faqList.get(position);
        holder.bind(faqItem);
    }
    @Override
    public int getItemCount() {
        return faqList.size();
    }
    static class FAQViewHolder extends RecyclerView.ViewHolder {
        private TextView text_question;
        private TextView text_answer;
        private boolean isExpanded = false;
        FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            text_question = itemView.findViewById(R.id.text_question);
            text_answer = itemView.findViewById(R.id.text_answer);
            text_question.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                text_answer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            });
        }
        void bind(FAQItem faqItem) {
            text_question.setText(faqItem.getQuestion());
            text_answer.setText(faqItem.getAnswer());
            text_answer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }
}

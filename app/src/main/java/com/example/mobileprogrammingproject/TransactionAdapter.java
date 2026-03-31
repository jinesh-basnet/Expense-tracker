package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) { this.list = list; }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup p, int vt) {
        return new TransactionViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_transaction, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder h, int pos) {
        Transaction t = list.get(pos);
        h.cat.setText(t.getCategory());
        h.desc.setText(t.getDescription());
        h.date.setText(t.getDate());
        h.amt.setText((t.isExpense() ? "-NRS " : "+NRS ") + t.getAmount());
        h.amt.setTextColor(Color.parseColor(t.isExpense() ? "#E53935" : "#4CAF50"));

        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddTransactionActivity.class);
            intent.putExtra("transaction", t); 
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView cat, desc, date, amt;
        public TransactionViewHolder(@NonNull View v) {
            super(v);
            cat = v.findViewById(R.id.tvTransactionCategory);
            desc = v.findViewById(R.id.tvTransactionDescription);
            date = v.findViewById(R.id.tvTransactionDate);
            amt = v.findViewById(R.id.tvTransactionAmount);
        }
    }
}

package com.example.mobileprogrammingproject;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvCategory.setText(transaction.getCategory());
        holder.tvDescription.setText(transaction.getDescription());
        holder.tvDate.setText(transaction.getDate());
        
        if (transaction.isExpense()) {
            holder.tvAmount.setText("-NRS " + transaction.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#E53935")); // Red
        } else {
            holder.tvAmount.setText("+NRS " + transaction.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50")); // Green
        }

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), AddTransactionActivity.class);
            intent.putExtra("transaction", transaction); 
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDescription, tvDate, tvAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvTransactionCategory);
            tvDescription = itemView.findViewById(R.id.tvTransactionDescription);
            tvDate = itemView.findViewById(R.id.tvTransactionDate);
            tvAmount = itemView.findViewById(R.id.tvTransactionAmount);
        }
    }
}

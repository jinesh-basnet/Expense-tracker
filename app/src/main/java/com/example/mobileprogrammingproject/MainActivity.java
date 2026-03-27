package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private DatabaseHelper databaseHelper;
    private TextView tvTotalBalance, tvTotalIncome, tvTotalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);
        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);

        recyclerView = findViewById(R.id.recyclerViewTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        // Add Swipe to Delete
        androidx.recyclerview.widget.ItemTouchHelper itemTouchHelper = new androidx.recyclerview.widget.ItemTouchHelper(new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Transaction transaction = transactionList.get(position);
                databaseHelper.deleteTransaction(transaction.getId());
                transactionList.remove(position);
                adapter.notifyItemRemoved(position);
                fetchAndRefresh(); // update counters
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fabAddTransaction);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTransactionActivity.class));
        });
    }

    public void onResume() {
        super.onResume();
        Log.d("MAIN", "Activity resumed, loading data...");
        fetchAndRefresh();
    }

    // method to get data and update the UI
    private void fetchAndRefresh() {
        Log.d("MAIN", "Refreshing transaction list");
        transactionList.clear();
        transactionList.addAll(databaseHelper.getAllTransactions());
        adapter.notifyDataSetChanged();
        
        calculateTotals();
    }

    private void calculateTotals() {
        Log.d("MAIN", "Recalculating totals...");
        double total = 0;
        double income = 0;
        double expense = 0;
        
        for (Transaction transaction : transactionList) {
            try {
                double amt = Double.parseDouble(transaction.getAmount());
                if (transaction.isExpense()) {
                    total -= amt;
                    expense += amt;
                } else {
                    total += amt;
                    income += amt;
                }
            } catch (Exception e) {
                // simple skip
            }
        }
        
        // update the text views
        tvTotalBalance.setText(String.format("$%.2f", total));
        tvTotalIncome.setText(String.format("+$%.2f", income));
        tvTotalExpense.setText(String.format("-$%.2f", expense));
    }
}
package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvUserName, tvHomeBalance;
    private CardView cardAdd, cardHistory;
    private ImageButton btnHomeLogout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);

        tvUserName = findViewById(R.id.tvUserName);
        tvHomeBalance = findViewById(R.id.tvHomeBalance);
        cardAdd = findViewById(R.id.cardAdd);
        cardHistory = findViewById(R.id.cardHistory);
        btnHomeLogout = findViewById(R.id.btnHomeLogout);

        // Standard actions
        cardAdd.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddTransactionActivity.class));
        });

        cardHistory.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        });

        btnHomeLogout.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Signing out...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });

        loadStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
    }

    private void loadStats() {
        List<Transaction> list = dbHelper.getAllTransactions();
        double total = 0;
        for (Transaction t : list) {
            try {
                double amt = Double.parseDouble(t.getAmount());
                if (t.isExpense()) {
                    total -= amt;
                } else {
                    total += amt;
                }
            } catch (Exception e) {}
        }
        tvHomeBalance.setText(String.format("$%.2f", total));
    }
}

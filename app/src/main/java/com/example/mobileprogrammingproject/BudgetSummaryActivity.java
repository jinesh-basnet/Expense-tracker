package com.example.mobileprogrammingproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class BudgetSummaryActivity extends AppCompatActivity {

    private ListView lvBudgetSummary;
    private TextView tvBudgetEmpty, tvBudgetSubtitle;
    private Button btnShowExpenses, btnShowIncome;
    private DatabaseHelper dbHelper;
    private boolean showingExpenses = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_summary);

        Toolbar toolbar = findViewById(R.id.toolbarBudget);
        toolbar.setNavigationOnClickListener(v -> finish());

        lvBudgetSummary = findViewById(R.id.lvBudgetSummary);
        tvBudgetEmpty = findViewById(R.id.tvBudgetEmpty);
        tvBudgetSubtitle = findViewById(R.id.tvBudgetSubtitle);
        btnShowExpenses = findViewById(R.id.btnShowExpenses);
        btnShowIncome = findViewById(R.id.btnShowIncome);
        dbHelper = new DatabaseHelper(this);

        btnShowExpenses.setOnClickListener(v -> {
            showingExpenses = true;
            updateToggleButtons();
            loadData();
        });

        btnShowIncome.setOnClickListener(v -> {
            showingExpenses = false;
            updateToggleButtons();
            loadData();
        });

        loadData();
    }

    private void updateToggleButtons() {
        if (showingExpenses) {
            btnShowExpenses.setBackgroundTintList(getResources().getColorStateList(R.color.expense_red, null));
            btnShowExpenses.setTextColor(getResources().getColor(R.color.white, null));
            btnShowIncome.setBackgroundTintList(getResources().getColorStateList(R.color.surface_variant, null));
            btnShowIncome.setTextColor(getResources().getColor(R.color.text_muted, null));
            tvBudgetSubtitle.setText("Showing expense categories");
        } else {
            btnShowIncome.setBackgroundTintList(getResources().getColorStateList(R.color.income_green, null));
            btnShowIncome.setTextColor(getResources().getColor(R.color.white, null));
            btnShowExpenses.setBackgroundTintList(getResources().getColorStateList(R.color.surface_variant, null));
            btnShowExpenses.setTextColor(getResources().getColor(R.color.text_muted, null));
            tvBudgetSubtitle.setText("Showing income categories");
        }
    }

    private void loadData() {
        int userId = getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) return;

        List<String[]> categoryTotals = dbHelper.getCategoryTotals(userId, showingExpenses);

        if (categoryTotals.isEmpty()) {
            lvBudgetSummary.setVisibility(View.GONE);
            tvBudgetEmpty.setVisibility(View.VISIBLE);
        } else {
            lvBudgetSummary.setVisibility(View.VISIBLE);
            tvBudgetEmpty.setVisibility(View.GONE);
            BudgetSummaryAdapter adapter = new BudgetSummaryAdapter(this, categoryTotals, showingExpenses);
            lvBudgetSummary.setAdapter(adapter);
        }
    }
}

package com.example.mobileprogrammingproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvStatsTotalIncome, tvStatsTotalExpense, tvStatsNetBalance, tvStatsEmpty;
    private TableLayout tableStats;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.toolbarStats);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvStatsTotalIncome = findViewById(R.id.tvStatsTotalIncome);
        tvStatsTotalExpense = findViewById(R.id.tvStatsTotalExpense);
        tvStatsNetBalance = findViewById(R.id.tvStatsNetBalance);
        tvStatsEmpty = findViewById(R.id.tvStatsEmpty);
        tableStats = findViewById(R.id.tableStats);
        dbHelper = new DatabaseHelper(this);

        loadData();
    }

    private void loadData() {
        int userId = getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) return;

        List<String[]> monthlyData = dbHelper.getMonthlyStats(userId);

        if (monthlyData.isEmpty()) {
            tvStatsEmpty.setVisibility(android.view.View.VISIBLE);
            return;
        }

        double totalIncome = 0, totalExpense = 0;

        int childCount = tableStats.getChildCount();
        if (childCount > 1) {
            tableStats.removeViews(1, childCount - 1);
        }

        boolean alternate = false;
        for (String[] row : monthlyData) {
            String month = row[0];
            double income = Double.parseDouble(row[1]);
            double expense = Double.parseDouble(row[2]);
            double net = income - expense;

            totalIncome += income;
            totalExpense += expense;

            TableRow tableRow = new TableRow(this);
            if (alternate) {
                tableRow.setBackgroundColor(Color.parseColor("#F6F9FC"));
            }
            alternate = !alternate;

            tableRow.addView(createCell(month, Color.parseColor("#2D3436"), false));
            tableRow.addView(createCell(String.format("%.0f", income), Color.parseColor("#00B894"), false));
            tableRow.addView(createCell(String.format("%.0f", expense), Color.parseColor("#D63031"), false));

            TextView netCell = createCell(String.format("%.0f", net), net >= 0 ? Color.parseColor("#00B894") : Color.parseColor("#D63031"), true);
            tableRow.addView(netCell);

            tableStats.addView(tableRow);
        }

        TableRow totalRow = new TableRow(this);
        totalRow.setBackgroundColor(Color.parseColor("#30336B"));

        totalRow.addView(createCell("TOTAL", Color.WHITE, true));
        totalRow.addView(createCell(String.format("%.0f", totalIncome), Color.parseColor("#00E676"), true));
        totalRow.addView(createCell(String.format("%.0f", totalExpense), Color.parseColor("#FF5252"), true));
        double totalNet = totalIncome - totalExpense;
        totalRow.addView(createCell(String.format("%.0f", totalNet), totalNet >= 0 ? Color.parseColor("#00E676") : Color.parseColor("#FF5252"), true));
        tableStats.addView(totalRow);

        tvStatsTotalIncome.setText(String.format("NRS %.0f", totalIncome));
        tvStatsTotalExpense.setText(String.format("NRS %.0f", totalExpense));
        tvStatsNetBalance.setText(String.format("NRS %.0f", totalNet));
        tvStatsNetBalance.setTextColor(totalNet >= 0 ?
                getResources().getColor(R.color.income_green, null) :
                getResources().getColor(R.color.expense_red, null));
    }

    private TextView createCell(String text, int color, boolean bold) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setPadding(32, 28, 32, 28);
        tv.setTextSize(13);
        tv.setGravity(Gravity.END);
        if (bold) {
            tv.setTypeface(null, Typeface.BOLD);
        }
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tv.setLayoutParams(params);
        return tv;
    }
}

package com.example.mobileprogrammingproject;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class CategoryGridActivity extends AppCompatActivity {

    private GridView gvCategories;
    private Button btnGridExpense, btnGridIncome;
    private DatabaseHelper dbHelper;
    private boolean showingExpenses = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_grid);

        Toolbar toolbar = findViewById(R.id.toolbarCategoryGrid);
        toolbar.setNavigationOnClickListener(v -> finish());

        gvCategories = findViewById(R.id.gvCategories);
        btnGridExpense = findViewById(R.id.btnGridExpense);
        btnGridIncome = findViewById(R.id.btnGridIncome);
        dbHelper = new DatabaseHelper(this);

        btnGridExpense.setOnClickListener(v -> {
            showingExpenses = true;
            updateToggleButtons();
            loadGrid();
        });

        btnGridIncome.setOnClickListener(v -> {
            showingExpenses = false;
            updateToggleButtons();
            loadGrid();
        });

        gvCategories.setOnItemClickListener((parent, view, position, id) -> {
            String[] item = (String[]) parent.getItemAtPosition(position);
            String category = item[0];
            String total = item[1];
            String count = item[2];
            Toast.makeText(this,
                    category + "\n" + count + " transaction(s)\nTotal: NRS " + String.format("%.2f", Double.parseDouble(total)),
                    Toast.LENGTH_SHORT).show();
        });

        loadGrid();
    }

    private void updateToggleButtons() {
        if (showingExpenses) {
            btnGridExpense.setBackgroundTintList(getResources().getColorStateList(R.color.expense_red, null));
            btnGridExpense.setTextColor(getResources().getColor(R.color.white, null));
            btnGridIncome.setBackgroundTintList(getResources().getColorStateList(R.color.surface_variant, null));
            btnGridIncome.setTextColor(getResources().getColor(R.color.text_muted, null));
        } else {
            btnGridIncome.setBackgroundTintList(getResources().getColorStateList(R.color.income_green, null));
            btnGridIncome.setTextColor(getResources().getColor(R.color.white, null));
            btnGridExpense.setBackgroundTintList(getResources().getColorStateList(R.color.surface_variant, null));
            btnGridExpense.setTextColor(getResources().getColor(R.color.text_muted, null));
        }
    }

    private void loadGrid() {
        int userId = getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) return;

        String[] categories = getResources().getStringArray(showingExpenses ? R.array.expense_categories : R.array.income_categories);
        List<String[]> categoryTotals = dbHelper.getCategoryTotals(userId, showingExpenses);

        String[][] gridData = new String[categories.length][3];
        for (int i = 0; i < categories.length; i++) {
            gridData[i][0] = categories[i];
            gridData[i][1] = "0";
            gridData[i][2] = "0";
            for (String[] ct : categoryTotals) {
                if (ct[0].equals(categories[i])) {
                    gridData[i][1] = ct[1];
                    gridData[i][2] = ct[2];
                    break;
                }
            }
        }

        CategoryGridAdapter adapter = new CategoryGridAdapter(this, gridData, showingExpenses);
        gvCategories.setAdapter(adapter);
    }
}

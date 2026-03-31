package com.example.mobileprogrammingproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDate, etDescription;
    private Spinner spinnerCategory;
    private RadioGroup rgType;
    private Button btnSave;
    private DatabaseHelper databaseHelper;
    private Transaction existingTransaction = null; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        databaseHelper = new DatabaseHelper(this);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        rgType = findViewById(R.id.rgType);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveTransaction());
        etDate.setOnClickListener(v -> showDatePicker());
        rgType.setOnCheckedChangeListener((group, checkedId) -> updateCategories(checkedId == R.id.rbExpense));

        updateCategories(rgType.getCheckedRadioButtonId() == R.id.rbExpense);

        if (getIntent().hasExtra("transaction")) {
            existingTransaction = (Transaction) getIntent().getSerializableExtra("transaction");
            setTitle("Edit Transaction");
            btnSave.setText("Update");
            preFillFields();
        }
    }

    private void updateCategories(boolean isExpense) {
        int arrayId = isExpense ? R.array.expense_categories : R.array.income_categories;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void preFillFields() {
        if (existingTransaction == null) return;
        etAmount.setText(existingTransaction.getAmount());
        etDescription.setText(existingTransaction.getDescription());
        etDate.setText(existingTransaction.getDate());
        rgType.check(existingTransaction.isExpense() ? R.id.rbExpense : R.id.rbIncome);
        
        for (int i = 0; i < spinnerCategory.getCount(); i++) {
            if (spinnerCategory.getItemAtPosition(i).toString().equals(existingTransaction.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            etDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveTransaction() {
        String category = spinnerCategory.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (amount.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) description = category;

        boolean isExpense = rgType.getCheckedRadioButtonId() == R.id.rbExpense;
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
        
        if (userId == -1) return;

        Transaction transaction = new Transaction(userId, category, description, amount, date, isExpense);
        
        if (existingTransaction != null) {
            transaction.setId(existingTransaction.getId());
            if (databaseHelper.updateTransaction(transaction) > 0) {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            if (databaseHelper.insertTransaction(transaction) > -1) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

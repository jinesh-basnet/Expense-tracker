package com.example.mobileprogrammingproject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

        if (getIntent().hasExtra("transaction")) {
            existingTransaction = (Transaction) getIntent().getSerializableExtra("transaction");
            setTitle("Edit Transaction");
            btnSave.setText("Update");
            preFillFields();
        }

        btnSave.setOnClickListener(v -> saveTransaction());
        etDate.setOnClickListener(v -> showDatePicker());
    }

    private void preFillFields() {
        if (existingTransaction == null) return;
        
        etAmount.setText(existingTransaction.getAmount());
        etDescription.setText(existingTransaction.getDescription());
        etDate.setText(existingTransaction.getDate());
        
        if (existingTransaction.isExpense()) {
            rgType.check(R.id.rbExpense);
        } else {
            rgType.check(R.id.rbIncome);
        }
        
        for (int i = 0; i < spinnerCategory.getCount(); i++) {
            if (spinnerCategory.getItemAtPosition(i).toString().equals(existingTransaction.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    etDate.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveTransaction() {
        Log.d("ADD_TRANS", "Save button clicked");
        String category = spinnerCategory.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (amount.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Empty fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            description = category; 
        }

        int selectedId = rgType.getCheckedRadioButtonId();
        boolean isExpense = true;
        
        if (selectedId == R.id.rbIncome) {
            isExpense = false;
        }

        Transaction transaction = new Transaction(category, description, amount, date, isExpense);
        
        if (existingTransaction != null) {
            transaction.setId(existingTransaction.getId());
            int rows = databaseHelper.updateTransaction(transaction);
            if (rows > 0) {
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            long id = databaseHelper.insertTransaction(transaction);
            if (id > -1) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.e("ADD_TRANS", "Database error!");
            }
        }
    }
}

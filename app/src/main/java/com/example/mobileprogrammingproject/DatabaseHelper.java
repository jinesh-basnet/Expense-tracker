package com.example.mobileprogrammingproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Common columns
    public static final String COLUMN_ID = "id";

    // Transactions table columns
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE = "type"; // "income" or "expense"

    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_AMOUNT + " REAL NOT NULL," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DATE + " TEXT NOT NULL," +
                    COLUMN_TYPE + " TEXT NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // save a new transaction to the db
    public long insertTransaction(Transaction transaction) {
        Log.d("DB_HELPER", "Inserting transaction: " + transaction.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // simple conversion logic
        double amountVal = 0;
        try {
            amountVal = Double.parseDouble(transaction.getAmount());
        } catch (Exception e) {
            Log.e("DB_HELPER", "Error parsing amount!");
        }

        values.put(COLUMN_AMOUNT, amountVal);
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_TYPE, transaction.isExpense() ? "expense" : "income");

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    // get everything from the table
    public List<Transaction> getAllTransactions() {
        Log.d("DB_HELPER", "Fetching all transactions...");
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int colId = cursor.getColumnIndex(COLUMN_ID);
                int colAmount = cursor.getColumnIndex(COLUMN_AMOUNT);
                int colCategory = cursor.getColumnIndex(COLUMN_CATEGORY);
                int colDate = cursor.getColumnIndex(COLUMN_DATE);
                int colType = cursor.getColumnIndex(COLUMN_TYPE);
                
                if(colId == -1 || colAmount == -1 || colCategory == -1 || colDate == -1 || colType == -1) continue;

                int id = cursor.getInt(colId);
                double amount = cursor.getDouble(colAmount);
                String category = cursor.getString(colCategory);
                
                // Get the description safely
                int colDescription = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                String description = colDescription != -1 ? cursor.getString(colDescription) : "Transaction";
                
                String date = cursor.getString(colDate);
                String type = cursor.getString(colType);

                boolean isExpense = "expense".equals(type);
                
                Transaction transaction = new Transaction(id, category, description, String.valueOf(amount), date, isExpense);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    // delete one row by id
    public void deleteTransaction(int id) {
        Log.d("DB_HELPER", "Deleting ID: " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

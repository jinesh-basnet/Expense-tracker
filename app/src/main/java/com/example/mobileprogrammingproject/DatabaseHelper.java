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
    private static final int DATABASE_VERSION = 2;

    // Table names
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Common columns
    public static final String COLUMN_ID = "id";

    // Transactions table columns
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_USER_ID_FK = "user_id"; // Foreign key

    // Users table columns
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_AMOUNT + " REAL NOT NULL," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DATE + " TEXT NOT NULL," +
                    COLUMN_TYPE + " TEXT NOT NULL," +
                    COLUMN_USER_ID_FK + " INTEGER NOT NULL" +
                    ")";

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_NAME + " TEXT NOT NULL," +
                    COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE," +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTIONS + " ADD COLUMN " + COLUMN_USER_ID_FK + " INTEGER DEFAULT 0");
        }
    }

    public long insertTransaction(Transaction transaction) {
        Log.d("DB_HELPER", "Inserting transaction: " + transaction.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

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
        values.put(COLUMN_USER_ID_FK, transaction.getUserId()); // Added

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    public List<Transaction> getAllTransactions(int userId) {
        Log.d("DB_HELPER", "Fetching transactions for user ID: " + userId);
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + 
                      " WHERE " + COLUMN_USER_ID_FK + " = ?" +
                      " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int colId = cursor.getColumnIndex(COLUMN_ID);
                int colAmount = cursor.getColumnIndex(COLUMN_AMOUNT);
                int colCategory = cursor.getColumnIndex(COLUMN_CATEGORY);
                int colDate = cursor.getColumnIndex(COLUMN_DATE);
                int colType = cursor.getColumnIndex(COLUMN_TYPE);
                int colUserId = cursor.getColumnIndex(COLUMN_USER_ID_FK);
                
                if(colId == -1 || colAmount == -1 || colCategory == -1 || colDate == -1 || colType == -1) continue;

                int id = cursor.getInt(colId);
                double amount = cursor.getDouble(colAmount);
                String category = cursor.getString(colCategory);
                
                int colDescription = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                String description = colDescription != -1 ? cursor.getString(colDescription) : "Transaction";
                
                String date = cursor.getString(colDate);
                String type = cursor.getString(colType);

                boolean isExpense = "expense".equals(type);
                
                Transaction transaction = new Transaction(id, userId, category, description, String.valueOf(amount), date, isExpense);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    public void deleteTransaction(int id) {
        Log.d("DB_HELPER", "Deleting ID: " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int updateTransaction(Transaction transaction) {
        Log.d("DB_HELPER", "Updating transaction ID: " + transaction.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

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
        values.put(COLUMN_USER_ID_FK, transaction.getUserId());

        int rowsAffected = db.update(TABLE_TRANSACTIONS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(transaction.getId())});
        db.close();
        return rowsAffected;
    }


    public long insertUser(String name, String email, String password) {
        Log.d("DB_HELPER", "Registering new user: " + email);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public int checkUser(String email, String password) {
        Log.d("DB_HELPER", "Checking login for: " + email);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + 
                      " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        String name = "User";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return name;
    }

}

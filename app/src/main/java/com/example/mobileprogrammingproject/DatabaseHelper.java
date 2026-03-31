package com.example.mobileprogrammingproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_USER_ID_FK = "user_id";

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_AMOUNT + " REAL NOT NULL," + COLUMN_CATEGORY + " TEXT," + COLUMN_DESCRIPTION + " TEXT," + COLUMN_DATE + " TEXT NOT NULL," + COLUMN_TYPE + " TEXT NOT NULL," + COLUMN_USER_ID_FK + " INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT NOT NULL," + COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE," + COLUMN_USER_PASSWORD + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) db.execSQL("ALTER TABLE " + TABLE_TRANSACTIONS + " ADD COLUMN " + COLUMN_USER_ID_FK + " INTEGER DEFAULT 0");
    }

    public long insertTransaction(Transaction t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_AMOUNT, Double.parseDouble(t.getAmount()));
        v.put(COLUMN_CATEGORY, t.getCategory());
        v.put(COLUMN_DESCRIPTION, t.getDescription());
        v.put(COLUMN_DATE, t.getDate());
        v.put(COLUMN_TYPE, t.isExpense() ? "expense" : "income");
        v.put(COLUMN_USER_ID_FK, t.getUserId());
        long result = db.insert(TABLE_TRANSACTIONS, null, v);
        db.close();
        return result;
    }

    public List<Transaction> getAllTransactions(int userId) {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_USER_ID_FK + " = ? ORDER BY " + COLUMN_ID + " DESC", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
                double amt = c.getDouble(c.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String cat = c.getString(c.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String desc = c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String date = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE));
                String type = c.getString(c.getColumnIndexOrThrow(COLUMN_TYPE));
                list.add(new Transaction(id, userId, cat, desc, String.valueOf(amt), date, "expense".equals(type)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int updateTransaction(Transaction t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_AMOUNT, t.getAmount());
        v.put(COLUMN_CATEGORY, t.getCategory());
        v.put(COLUMN_DESCRIPTION, t.getDescription());
        v.put(COLUMN_DATE, t.getDate());
        v.put(COLUMN_TYPE, t.isExpense() ? "expense" : "income");
        v.put(COLUMN_USER_ID_FK, t.getUserId());
        int rows = db.update(TABLE_TRANSACTIONS, v, COLUMN_ID + " = ?", new String[]{String.valueOf(t.getId())});
        db.close();
        return rows;
    }

    public long insertUser(String name, String email, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_USER_NAME, name);
        v.put(COLUMN_USER_EMAIL, email);
        v.put(COLUMN_USER_PASSWORD, pass);
        long id = db.insert(TABLE_USERS, null, v);
        db.close();
        return id;
    }

    public int checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?", new String[]{email, pass});
        int userId = -1;
        if (c.moveToFirst()) userId = c.getInt(0);
        c.close();
        db.close();
        return userId;
    }

    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COLUMN_USER_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        String name = "User";
        if (c.moveToFirst()) name = c.getString(0);
        c.close();
        db.close();
        return name;
    }

    public List<String[]> getCategoryTotals(int userId, boolean isExpense) {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String type = isExpense ? "expense" : "income";
        Cursor c = db.rawQuery("SELECT " + COLUMN_CATEGORY + ", SUM(" + COLUMN_AMOUNT + "), COUNT(*) FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_TYPE + " = ? GROUP BY " + COLUMN_CATEGORY + " ORDER BY SUM(" + COLUMN_AMOUNT + ") DESC", new String[]{String.valueOf(userId), type});
        if (c.moveToFirst()) {
            do {
                list.add(new String[]{c.getString(0), String.valueOf(c.getDouble(1)), String.valueOf(c.getInt(2))});
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public List<String[]> getMonthlyStats(int userId) {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUBSTR(" + COLUMN_DATE + ", 1, 7) AS month, SUM(CASE WHEN " + COLUMN_TYPE + " = 'income' THEN " + COLUMN_AMOUNT + " ELSE 0 END) AS inc, SUM(CASE WHEN " + COLUMN_TYPE + " = 'expense' THEN " + COLUMN_AMOUNT + " ELSE 0 END) AS exp FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_USER_ID_FK + " = ? GROUP BY month ORDER BY month DESC", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            do {
                list.add(new String[]{c.getString(0), String.valueOf(c.getDouble(1)), String.valueOf(c.getDouble(2))});
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }
}

package com.example.mobileprogrammingproject;

import java.io.Serializable;

public class Transaction implements Serializable {
    private int id, userId;
    private String category, description, amount, date;
    private boolean isExpense;

    public Transaction(int id, int userId, String cat, String desc, String amt, String d, boolean exp) {
        this.id = id; this.userId = userId; this.category = cat; this.description = desc; this.amount = amt; this.date = d; this.isExpense = exp;
    }

    public Transaction(int userId, String cat, String desc, String amt, String d, boolean exp) {
        this.userId = userId; this.category = cat; this.description = desc; this.amount = amt; this.date = d; this.isExpense = exp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getAmount() { return amount; }
    public String getDate() { return date; }
    public boolean isExpense() { return isExpense; }
}

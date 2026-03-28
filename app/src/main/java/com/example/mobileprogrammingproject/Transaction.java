package com.example.mobileprogrammingproject;

import java.io.Serializable;

public class Transaction implements Serializable {
    private int id;
    private int userId; 
    private String category;
    private String description;
    private String amount;
    private String date;
    private boolean isExpense;

    public Transaction(int id, int userId, String category, String description, String amount, String date, boolean isExpense) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.isExpense = isExpense;
    }

    public Transaction(int userId, String category, String description, String amount, String date, boolean isExpense) {
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.isExpense = isExpense;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public boolean isExpense() {
        return isExpense;
    }
}

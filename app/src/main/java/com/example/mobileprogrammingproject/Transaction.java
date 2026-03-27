package com.example.mobileprogrammingproject;

public class Transaction {
    private int id;
    private String category;
    private String description;
    private String amount;
    private String date;
    private boolean isExpense;

    public Transaction(int id, String category, String description, String amount, String date, boolean isExpense) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.isExpense = isExpense;
    }

    public Transaction(String category, String description, String amount, String date, boolean isExpense) {
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

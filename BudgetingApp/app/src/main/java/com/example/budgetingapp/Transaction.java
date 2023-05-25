package com.example.budgetingapp;

public class Transaction implements Comparable<Transaction> {
    public final String amount;
    public final String date;
    public final String account;
    public final String note;
    public final String color;
    public final String envelope;

    Transaction(String amount, String date, String account, String note, String color, String envelope) {
        this.amount = amount;
        this.date = date;
        this.account = account;
        this.note = note;
        this.color = color;
        this.envelope = envelope;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getAccount() {
        return account;
    }

    public String getNote() {
        return note;
    }

    public String getColor() {
        return color;
    }

    public String getEnvelope() {
        return envelope;
    }

    @Override
    public int compareTo(Transaction other) {
        return date.compareTo(other.date);
    }
}

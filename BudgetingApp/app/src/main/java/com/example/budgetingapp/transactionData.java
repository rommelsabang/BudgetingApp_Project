package com.example.budgetingapp;

public class transactionData {
    String date;
    String note;
    String envelope;
    String amount;

    public transactionData(String date, String note, String envelope, String amount) {
        this.date = date;
        this.note = note;
        this.envelope = envelope;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getEnvelope() {
        return envelope;
    }

    public String getAmount() {
        return amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEnvelope(String envelope) {
        this.envelope = envelope;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

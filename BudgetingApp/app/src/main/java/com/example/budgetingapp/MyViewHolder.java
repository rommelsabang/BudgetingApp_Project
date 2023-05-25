package com.example.budgetingapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView note;
    TextView envelope;
    TextView amount;
    TextView date;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        note = itemView.findViewById(R.id.note);
        envelope = itemView.findViewById(R.id.envelope);
        amount = itemView.findViewById(R.id.amount);
        date = itemView.findViewById(R.id.date);

    }
}

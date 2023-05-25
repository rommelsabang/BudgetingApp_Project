package com.example.budgetingapp;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<transactionData> items;

    public MyAdapter(Context context, List<transactionData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_items, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date.setText(items.get(position).getDate());
        holder.envelope.setText(items.get(position).getEnvelope());
        holder.amount.setText(items.get(position).getAmount());
        holder.note.setText(items.get(position).getNote());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

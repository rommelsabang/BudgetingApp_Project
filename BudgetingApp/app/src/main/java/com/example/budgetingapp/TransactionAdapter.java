package com.example.budgetingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private final Context mContext;
    private final int mResource;

    public TransactionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Transaction> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView txtAmount = convertView.findViewById(R.id.amount);
        TextView txtDate = convertView.findViewById(R.id.date);
        TextView txtAccount = convertView.findViewById(R.id.account);
        TextView txtNote = convertView.findViewById(R.id.note);
        TextView txtEnvelope = convertView.findViewById(R.id.envelope);

        String amountString = "$" + getItem(position).getAmount().replace("-", "");

        if (Double.parseDouble(getItem(position).getAmount()) < 0) {
            amountString = "- " + amountString;
            txtAmount.setTextColor(Color.parseColor("#F3650a"));
        } else {
            txtAmount.setTextColor(Color.parseColor("#96be25"));
        }

        String colorString = getItem(position).getColor().replace("#", "#88");
        convertView.setBackgroundColor(Color.parseColor(colorString));


        txtAccount.setText(getItem(position).getAccount());
        txtAmount.setText(amountString);
        txtDate.setText(getItem(position).getDate());
        txtNote.setText(getItem(position).getNote());
        txtEnvelope.setText(getItem(position).getEnvelope());

        return convertView;
    }
}

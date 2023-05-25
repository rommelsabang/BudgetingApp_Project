package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.budgetingapp.databinding.ActivityAccountDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class Account_details extends DrawerBaseActivity {

    ActivityAccountDetailsBinding activityAccountDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountDetailsBinding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAccountDetailsBinding.getRoot());

        allocateActivityTitle("Account Details");

        RecyclerView recyclerView = findViewById(R.id.rv);

        List<transactionData> items = new ArrayList<transactionData>();
        items.add(new transactionData("11/01/2022","Rent","Recurring","$850"));
        items.add(new transactionData("11/03/2022","Grocery", "Grocery", "$83.71"));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
    }
}
package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetingapp.databinding.ActivityAccountsBinding;
import com.example.budgetingapp.databinding.ActivityMainBinding;

public class Accounts extends DrawerBaseActivity {

    ActivityAccountsBinding activityAccountsBinding;

    TextView chequing;
    TextView saving;
    TextView credit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountsBinding = ActivityAccountsBinding.inflate(getLayoutInflater());
        setContentView(activityAccountsBinding.getRoot());



        chequing = findViewById(R.id.chequing);
        chequing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accounts.this,Account_details.class);

                startActivity(intent);
            }
        });

        saving = findViewById(R.id.saving);
        saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accounts.this,Account_details.class);

                startActivity(intent);
            }
        });

        credit = findViewById(R.id.credit);
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accounts.this,Account_details.class);

                startActivity(intent);
            }
        });

    }
}
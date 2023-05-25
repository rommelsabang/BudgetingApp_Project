package com.example.budgetingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetingapp.databinding.ActivityTransactionsOverviewBinding;

import java.io.FileOutputStream;
import java.io.IOException;

public class reports_activity extends DrawerBaseActivity {



    //{0:envelope,1:account}
    public int num =-1;
    //{0:month view,1:month over month view}
    public int num2 =-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_activity);

        Button currentMonthEnvelope = findViewById(R.id.button);
        Button currentMonthAccount = findViewById(R.id.button2);
        Button backButton = findViewById(R.id.button5);


        currentMonthEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reports_activity.this, reports_summary_activity_envelope.class);
                num =0;
                num2=0;
                intent.putExtra("monthView",num2);
                intent.putExtra("summaryType",num);
                startActivity(intent);
            }
        });

        currentMonthAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reports_activity.this, reports_summary_activity_envelope.class);
                num=1;
                num2=0;
                intent.putExtra("monthView",num2);
                intent.putExtra("summaryType",num);
                startActivity(intent);
            }
        });


        //using finish method, so it will go back to the activity that was open previous [ie another main task]
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }
}
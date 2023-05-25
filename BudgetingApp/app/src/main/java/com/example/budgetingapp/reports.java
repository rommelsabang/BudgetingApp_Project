package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.budgetingapp.databinding.ActivityReportsBinding;

public class reports extends DrawerBaseActivity {

    ActivityReportsBinding activityReportsBinding;

    //{0:envelope,1:account}
    public int num = -1;
    //{0:month view,1:month over month view}
    public int num2 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportsBinding = ActivityReportsBinding.inflate(getLayoutInflater());
        setContentView(activityReportsBinding.getRoot());

        Button currentMonthEnvelope = findViewById(R.id.button);
        Button currentMonthAccount = findViewById(R.id.button2);
        Button backButton = findViewById(R.id.button5);

        currentMonthEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reports.this, reports_summary_activity_envelope.class);
                num = 0;
                num2 = 0;
                intent.putExtra("monthView", num2);
                intent.putExtra("summaryType", num);
                startActivity(intent);
            }
        });

        currentMonthAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reports.this, reports_summary_activity_envelope.class);
                num = 1;
                num2 = 0;
                intent.putExtra("monthView", num2);
                intent.putExtra("summaryType", num);
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
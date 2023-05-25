package com.example.budgetingapp;

import static java.lang.Float.parseFloat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetingapp.databinding.ActivityReportsSummaryEnvelopeBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//https://github.com/PhilJay/MPAndroidChart
// maven { url 'https://jitpack.io' } paste into -> settings.gradle
//implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0' paste into dependencies -> build.gradle (app)
//sync gradle

public class reports_summary_activity_envelope extends DrawerBaseActivity implements AdapterView.OnItemSelectedListener {

    ActivityReportsSummaryEnvelopeBinding activityReportsSummaryEnvelopeBinding;

    public PieChart pieChart;
    public BarChart barChart;
    public ArrayList<String[]> split = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportsSummaryEnvelopeBinding = ActivityReportsSummaryEnvelopeBinding.inflate(getLayoutInflater());
        setContentView(activityReportsSummaryEnvelopeBinding.getRoot());

        Intent intent = getIntent();
        int viewTypeNum = intent.getIntExtra("summaryType",0);
        int monthView = intent.getIntExtra("monthView",0);
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        Button backButton = findViewById(R.id.button5);
        Button makeSelectionButton = findViewById(R.id.button6);
        TextView viewType = findViewById(R.id.textView3);
        TextView periodSpinnnerHeader = findViewById(R.id.textView4);
        Spinner spinner = findViewById(R.id.spinner);

        Spinner spinner2 = findViewById(R.id.spinner2);
        periodSpinnnerHeader.setText("Time Period");
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Months, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);




        setupPieChart();


        //defualt: Current month, by envelopes // defaults to transactions envelope, november
        if(viewTypeNum==0 && monthView==0){

            //Set correct spinners and text
            viewType.setText("Envelopes");

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Envelopes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner2.setSelection(0);
            spinner.setOnItemSelectedListener(this);


                ArrayList<String> moneySpent = new ArrayList<>();
                ArrayList<String> percentages = new ArrayList<>();
                ArrayList<String> categories = new ArrayList<>();
                String file = "transactions_2.csv";

                try{

                    FileInputStream fis = openFileInput(file);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);

                    String line2;

                    while((line2 = br.readLine()) != null){
                        split.add(line2.split(","));
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //for each transaction, check id, if id equals transactions id, then add money spent to
                //array list
                for(int i=0;i<split.size();i++){
                    if(Integer.parseInt(split.get(i)[0]) ==2){
                        //add dollar price,taking away the negative sign
                        moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-")+1));
                        //add category name
                        categories.add(split.get(i)[5]);
                    }
                }


                float budget = 1500;
                float remainingMoney=0;
                float moneyOut=0;
                for(String money:moneySpent){
                    float num = parseFloat(money);
                    double percent = num/budget;
                    moneyOut += num;
                    percentages.add(String.valueOf(percent)+"f");
                }
                remainingMoney = 1500-moneyOut;
                ArrayList<Integer> remainingBudgetArray = new ArrayList<>();
                remainingBudgetArray.add((int) remainingMoney);
                String[] envelopName = {"","","Transportation"};


                loadPieChartData(percentages,categories);
                setupBarChart();
                loadBarChartData(remainingBudgetArray,envelopName,"Remaining Budget");

            //defualt:Month over month, by Envelopes
        }else if(viewTypeNum==0 && monthView ==1){

            //Set correct spinners and text
            viewType.setText("Envelopes");

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Envelopes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner2.setSelection(1);
            spinner.setOnItemSelectedListener(this);

            //default: Current Month, by account
        }else if(viewTypeNum==1 && monthView ==0){

            //Set correct spinners and text
            viewType.setText("Accounts");

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Accounts, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner2.setSelection(0);
            spinner.setOnItemSelectedListener(this);
            try{

                String file = "transactions_2.csv";
                FileInputStream fis = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                String line2;

                while((line2 = br.readLine()) != null){
                    split.add(line2.split(","));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> moneySpent = new ArrayList<>();
            ArrayList<String> percentages = new ArrayList<>();
            ArrayList<String> categories = new ArrayList<>();

            //for each transaction, check id, if id equals transactions id, then add money spent to
            //array list
            for(int i=0;i<split.size();i++){
                if(split.get(i)[4].contains("RBC")){
                    //change this for default month
                    if(split.get(i)[3].contains("-11-")){
                        //making sure there is negative sign, to ensure money is being spent
                        if(split.get(i)[3].contains("-")){

                            //add dollar price,taking away the negative sign
                            moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-")+1));
                            //add category name
                            categories.add(split.get(i)[5]);
                        }
                    }

                }
            }


            float moneyOut=0;
            for(String money:moneySpent){
                float num = parseFloat(money);
                moneyOut+=num;
                percentages.add(String.valueOf(num)+"f");
            }

            ArrayList<Integer> moneySpentArray = new ArrayList<>();
            moneySpentArray.add((int) moneyOut);
            String[] accountName = {"","","RBC"};


            loadPieChartData(percentages,categories);
            setupBarChart();
            loadBarChartData(moneySpentArray,accountName,"Dollar Amount Spent");


            //default: Month over Month, by account
        }else if(viewTypeNum==1 && monthView ==1){

            //Set correct spinners and text
            viewType.setText("Accounts");

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Accounts, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner2.setSelection(1);
            spinner.setOnItemSelectedListener(this);
        }

        //loadPieChartData(percents,categories);

        //finish this activity, go back to previous
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        makeSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItem().toString().contains("Transportation") && spinner2.getSelectedItem().toString().contains("November")){

                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(Integer.parseInt(split.get(i)[0]) ==2){
                            if(split.get(i)[3].contains("-11-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }


                    float budget = 1500;
                    float remainingMoney=0;
                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        double percent = num/budget;
                        moneyOut += num;
                        percentages.add(String.valueOf(percent)+"f");
                    }
                    remainingMoney = 1500-moneyOut;
                    ArrayList<Integer> remainingBudgetArray = new ArrayList<>();
                    remainingBudgetArray.add((int) remainingMoney);
                    String[] envelopName = {"","","Transportation"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(remainingBudgetArray,envelopName,"Remaining Budget");

                }
                if(spinner.getSelectedItem().toString().contains("Transportation") && spinner2.getSelectedItem().toString().contains("December")){

                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(Integer.parseInt(split.get(i)[0]) ==2){
                            if(split.get(i)[3].contains("-12-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }


                    float budget = 1500;
                    float remainingMoney=0;
                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        double percent = num/budget;
                        moneyOut += num;
                        percentages.add(String.valueOf(percent)+"f");
                    }
                    remainingMoney = 1500-moneyOut;
                    ArrayList<Integer> remainingBudgetArray = new ArrayList<>();
                    remainingBudgetArray.add((int) remainingMoney);
                    String[] envelopName = {"","","Transportation"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(remainingBudgetArray,envelopName,"Remaining Budget");
                }

                if(spinner.getSelectedItem().toString().contains("Groceries") && spinner2.getSelectedItem().toString().contains("November")){
                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(Integer.parseInt(split.get(i)[0]) ==1){
                            if(split.get(i)[3].contains("-11-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }


                    float budget = 500;
                    float remainingMoney=0;
                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        double percent = num/budget;
                        moneyOut += num;
                        percentages.add(String.valueOf(num)+"f");
                    }
                    remainingMoney = 500-moneyOut;
                    ArrayList<Integer> remainingBudgetArray = new ArrayList<>();
                    remainingBudgetArray.add((int) remainingMoney);
                    String[] envelopName = {"","","Groceries"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(remainingBudgetArray,envelopName,"Remaining Budget");
                }
                if(spinner.getSelectedItem().toString().contains("Groceries") && spinner2.getSelectedItem().toString().contains("December")){
                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(Integer.parseInt(split.get(i)[0]) ==1){
                            if(split.get(i)[3].contains("-12-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }


                    float budget = 500;
                    float remainingMoney=0;
                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        double percent = num/budget;
                        moneyOut += num;
                        percentages.add(String.valueOf(num)+"f");
                    }
                    remainingMoney = 500-moneyOut;
                    ArrayList<Integer> remainingBudgetArray = new ArrayList<>();
                    remainingBudgetArray.add((int) remainingMoney);
                    String[] envelopName = {"","","Groceries"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(remainingBudgetArray,envelopName,"Remaining Budget");
                }
                if(spinner.getSelectedItem().toString().contains("RBC") && spinner2.getSelectedItem().toString().contains("November")){
                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(split.get(i)[4].contains("RBC")){
                            if(split.get(i)[3].contains("-11-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }

                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        moneyOut+=num;
                        percentages.add(String.valueOf(num)+"f");
                    }

                    ArrayList<Integer> moneySpentArray = new ArrayList<>();
                    moneySpentArray.add((int) moneyOut);
                    String[] accountName = {"","","RBC"};


                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(moneySpentArray,accountName,"Dollar Amount Spent");
                }if(spinner.getSelectedItem().toString().contains("RBC") && spinner2.getSelectedItem().toString().contains("December")){
                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(split.get(i)[4].contains("RBC")){
                            if(split.get(i)[3].contains("-12-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }

                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        moneyOut+=num;
                        percentages.add(String.valueOf(num)+"f");
                    }

                    ArrayList<Integer> moneySpentArray = new ArrayList<>();
                    moneySpentArray.add((int) moneyOut);
                    String[] accountName = {"","","RBC"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(moneySpentArray,accountName,"Dollar Amount Spent");
                }
                if(spinner.getSelectedItem().toString().contains("ScotiaBank") && spinner2.getSelectedItem().toString().contains("November")){
                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(split.get(i)[4].contains("Scotiabank")){
                            if(split.get(i)[3].contains("-11-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }

                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        moneyOut+=num;
                        percentages.add(String.valueOf(num)+"f");
                    }

                    ArrayList<Integer> moneySpentArray = new ArrayList<>();
                    moneySpentArray.add((int) moneyOut);
                    String[] accountName = {"","","Scotiabank"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(moneySpentArray,accountName,"Dollar Amount Spent");
                }
                if(spinner.getSelectedItem().toString().contains("ScotiaBank") && spinner2.getSelectedItem().toString().contains("December")){
                    ArrayList<String> moneySpent = new ArrayList<>();
                    ArrayList<String> percentages = new ArrayList<>();
                    ArrayList<String> categories = new ArrayList<>();

                    //for each transaction, check id, if id equals transactions id, then add money spent to
                    //array list
                    for(int i=0;i<split.size();i++){
                        if(split.get(i)[4].contains("Scotiabank")){
                            if(split.get(i)[3].contains("-12-")) {
                                if(split.get(i)[2].contains("-")){
                                    //add dollar price,taking away the negative sign
                                    moneySpent.add(split.get(i)[2].substring(split.get(i)[2].lastIndexOf("-") + 1));
                                    //add category name
                                    categories.add(split.get(i)[5]);
                                }
                            }
                        }
                    }

                    float moneyOut=0;
                    for(String money:moneySpent){
                        float num = parseFloat(money);
                        moneyOut+=num;
                        percentages.add(String.valueOf(num)+"f");
                    }

                    ArrayList<Integer> moneySpentArray = new ArrayList<>();
                    moneySpentArray.add((int) moneyOut);
                    String[] accountName = {"","","Scotiabank"};

                    if(percentages.size()==0){
                        Toast.makeText(getApplicationContext(), "No transactions during this time", Toast.LENGTH_SHORT).show();
                    }
                    loadPieChartData(percentages,categories);
                    setupBarChart();
                    loadBarChartData(moneySpentArray,accountName,"Dollar Amount Spent");
                }
            }
        });

    }
    private void loadBarChartData(ArrayList<Integer> remaining,String[] categories,String bardataString){

       ArrayList<BarEntry> barArrayList = new ArrayList();
        for(int i=0;i< remaining.size();i++){
            barArrayList.add(new BarEntry(Float.parseFloat(parseFloat(String.valueOf(i+2))+"f"),remaining.get(i)));
        }
//        barArrayList.add(new BarEntry(2f,10));
//        barArrayList.add(new BarEntry(3f,6));
//        barArrayList.add(new BarEntry(4f,20));
//        barArrayList.add(new BarEntry(5f,50));
        BarDataSet barDataSet = new BarDataSet(barArrayList, bardataString);


        //String[] xAxisLables = new String[]{"","","gas","electric", "housing", "entertainment"};

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(categories));
        barChart.getXAxis().setLabelCount(categories.length - 2);
        BarData theData = new BarData(barDataSet);
        theData.setValueTextSize(16f);
        barChart.setData(theData);
        barChart.invalidate();
    }

    private void setupBarChart(){

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.getDescription().setEnabled(true);

        Legend l2 = barChart.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setDrawInside(false);
        l2.setEnabled(true);
    }


    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }
    private void loadPieChartData(ArrayList<String> percents,ArrayList<String> Categories){
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for(int i=0;i< percents.size();i++){
            entries.add(new PieEntry(parseFloat(percents.get(i)),Categories.get(i)));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
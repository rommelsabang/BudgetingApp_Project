package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.budgetingapp.databinding.ActivityDashboardBinding;
import com.example.budgetingapp.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

import java.util.ArrayList;

public class Dashboard extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;

    private static final float BAR_SPACE = 0.05f;
    private static final float BAR_WIDTH = 0.15f;
    private static final float MAX_X_VALUE = 6f;

    private BarChart chart;

    private void createTransactionCSV() {
        //Data being written to CSV for demo purposes only
        String filename = "transactions.csv";
        String[] file_content = new String[5];
        file_content[0] = "1,Groceries,50.70,01-12-22,RBC,Save On Foods";
        file_content[1] = "2,Groceries,32.45,25-11-22,Scotiabank,Safeway";
        file_content[2] = "3,Transportation,80.50,25-10-22,RBC,Gas";
        file_content[3] = "4,Transportation,60.34,16-11-22,RBC,Gas";
        file_content[4] = "5,Transportation,71.05,29-11-22,Scotiabank,Gas";

        FileOutputStream outputStream;
        try {
            for (int i = 0; i < file_content.length; i++) {
                outputStream = openFileOutput(filename, Context.MODE_APPEND);
                outputStream.write(file_content[i].getBytes());
                outputStream.write("\n".getBytes());
                outputStream.close();
            }
            String line = "1, 1, -50.70, 29-11-22, RBC, Save On Foods\n"+"1, 2, -32.45, 30-11-22, Scotiabank, Safeway\n+" +
                    "1, 3, 100.00, 30-12-22, Scotiabank, Payday\n"+"2, 1, -80.50, 19-11-22, RBC, Gas\n"+"2, 2, -52.45, 29-11-22, Scotiabank, Gas\n"+
                    "2, 3, -2, 30-11-22, Scotiabank, Bus\n"+"2, 3, -220, 14-12-22, RBC, Insurance\n"+"2, 1, -24.45, 10-12-22, RBC, Car Wash\n";

            FileOutputStream writer = openFileOutput("transactions_2.csv", Context.MODE_APPEND);
            writer.write(line.getBytes());
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }
    private void createEnvelopeCSV() {
        //Data being written to CSV for demo purposes only
        String filename = "envelopes.csv";
        String[] file_content = new String[2];
        file_content[0] = "1500,Transportation,ffabffbe";
        file_content[1] = "500,Groceries,ffff9191";

        FileOutputStream outputStream;
        try {
            for (int i = 0; i < file_content.length; i++) {
                outputStream = openFileOutput(filename, Context.MODE_APPEND);
                outputStream.write(file_content[i].getBytes());
                outputStream.write("\n".getBytes());
                outputStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private BarData createChartData(int cur_month) {

        ArrayList<IBarDataSet> data_sets = new ArrayList<>();
        Scanner scan_envelopes = null;
        Scanner scan_transactions = null;

        //Create list of envelopes
        ArrayList<String> envelope_list = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("envelopes.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line == "")
                    break;
                envelope_list.add(line.split(",")[1]);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < envelope_list.size(); i++) {

            //Create 0 array of floats (one element for each month)
            float[] amounts = new float[12];
            for (int j = 0; j < amounts.length; j++)
                amounts[j] = 0F;

            //Loop over each transaction, check if it belongs to current envelope, if True add
            //transaction to array under correct month index
            try {
                FileInputStream fis = openFileInput("transactions.csv");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)  {
                    if (line == "")
                        break;
                    String[] line_split = line.split(",");
                    String trans_envelope = line_split[1];
                    if (trans_envelope.equals(envelope_list.get(i))) {
                        int month = Integer.parseInt(line_split[3].substring(3, 5)) - 1;
                        float amt = Float.parseFloat(line_split[2]);
                        amounts[month] = amounts[month] + amt;
                    }
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create list of bar entries for past 6 months
            ArrayList<BarEntry> bar_entries = new ArrayList<>();
            int n = 0;
            //Should wrap around to last year if cur_month <6
            for (int j = cur_month - 5; j <= cur_month; j++) {
                bar_entries.add(new BarEntry(n, amounts[j]));
                n++;
            }

            //Add bar entries to graph data set with label name
            BarDataSet bar_data_set = new BarDataSet(bar_entries, envelope_list.get(i));
            bar_data_set.setColor(ColorTemplate.MATERIAL_COLORS[i]);
            data_sets.add(bar_data_set);
        }

        BarData graph_data = new BarData(data_sets);
        return graph_data;
    }
    private void prepareChartData(BarData data) {
        chart.setData(data);
        chart.getBarData().setBarWidth(BAR_WIDTH);
        float groupSpace = 1f - ((BAR_SPACE + BAR_WIDTH) * 3);
        chart.groupBars(0, groupSpace, BAR_SPACE);
        chart.invalidate();
    }
    private void configureChartAppearance() {
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(true);
        chart.getDescription().setEnabled(false);

        chart.getXAxis().setEnabled(false);
        /*final String[] months = new String[] {"July", "August", "September", "October", "November", "December"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return months[(int) value];
            }
        };
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setCenterAxisLabels(true);
        */

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
        chart.invalidate();
    }
    private double getBudget() {
        double budget = 0.0;
        try {
            FileInputStream fis = openFileInput("envelopes.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line == "")
                    break;

                budget = budget + Double.parseDouble(line.split(",")[0]);
            }
        } catch(FileNotFoundException e) {
            return 0.0;
        } catch (IOException e) {
            return 0.0;
        }
        return budget;
    }
    private double getSpent(int cur_month) {
        double spent = 0.0;
        try {
            FileInputStream fis = openFileInput("transactions.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line == "")
                    break;
                int month = Integer.parseInt(line.split(",")[3].substring(3,5))-1;
                spent = spent + Double.parseDouble(line.split(",")[0]);
            }
        } catch(FileNotFoundException e) {
            return 0.0;
        } catch (IOException e) {
            return 0.0;
        }
        return spent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");

        //Check csv files exist; create if not
        try {
            FileInputStream fis = openFileInput("envelopes.csv");
        } catch (FileNotFoundException e) {
            createEnvelopeCSV();
        }
        try {
            FileInputStream fis = openFileInput("transactions.csv");
        } catch (FileNotFoundException e) {
            createTransactionCSV();
        }

        //Create chart
        chart = findViewById(R.id.barchart);
        BarData data = null;
        try {
            data = createChartData(11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareChartData(data);
        configureChartAppearance();

        //Set textView values
        Double amt_budget = getBudget();
        Double amt_spent = getSpent(11);
        Double amt_left = amt_budget - amt_spent;

        TextView txt_budget = findViewById(R.id.textView2);
        TextView txt_spent = findViewById(R.id.textView3);
        TextView txt_remaining = findViewById(R.id.textView4);

        txt_budget.setText("$" + String.format("%,.2f", amt_budget) + " Budget");
        txt_spent.setText("$" + String.format("%,.2f", amt_spent) + " Spent");
        txt_remaining.setText("$" + String.format("%,.2f", amt_left) + " Remaining");

    }
}

package com.example.budgetingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import com.example.budgetingapp.databinding.ActivityAddTransactionBinding;

public class AddTransaction extends DrawerBaseActivity {

    ActivityAddTransactionBinding activityAddTransactionBinding;

    boolean editing = false;
    int transactionPosition;

    boolean checksPassed = false;

    CheckBox incomeCheck;
    Spinner env_spinner, account_spinner;
    Button submitButton, deleteButton, datePickerButton, backButton;
    EditText noteEdit, amountEdit;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddTransactionBinding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(activityAddTransactionBinding.getRoot());

        allocateActivityTitle("Add Transaction");

        Intent intent = this.getIntent();

        //Check if we are editing
        if (intent.hasExtra("date")) {
            editing = true;

            allocateActivityTitle("Edit transaction");
        }

        //Initialize
        noteEdit = findViewById(R.id.note_edit);
        amountEdit = findViewById(R.id.amount_edit);
        incomeCheck = findViewById(R.id.incomeCheck);

        initDatePicker();
        datePickerButton = findViewById(R.id.date_picker_button);
        datePickerButton.setText(getTodayDate());
        datePickerButton.setOnClickListener(v -> {
            openDatePicker(v);
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent i = new Intent(AddTransaction.this, TransactionsOverview.class);
            startActivity(i);
        });

        //Populate Spinners
        env_spinner = findViewById(R.id.env_spinner);
        ArrayAdapter<CharSequence> env_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.env_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        env_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        env_spinner.setAdapter(env_adapter);

        account_spinner = findViewById(R.id.account_spinner);
        ArrayAdapter<CharSequence> acc_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.account_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        acc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(acc_adapter);

        //Editing behaviour
        if (editing) {
            Bundle bundle = intent.getExtras();

            //Set defaults to be those of transaction being edited
            //Set income check
            if ( Double.parseDouble(bundle.getString("amount")) >= 0) {
                incomeCheck.setChecked(true);
            }

            //Set spinners
            ArrayAdapter spinnerAdapter = (ArrayAdapter) env_spinner.getAdapter();
            int spinnerPosition = spinnerAdapter.getPosition(bundle.getString("envelope"));
            env_spinner.setSelection(spinnerPosition);

            spinnerAdapter = (ArrayAdapter) account_spinner.getAdapter();
            spinnerPosition = spinnerAdapter.getPosition(bundle.getString("account"));
            account_spinner.setSelection(spinnerPosition);

            //Set TextEdits
            datePickerButton.setText(bundle.getString("date"));
            amountEdit.setText(bundle.getString("amount").replace("-",""));
            noteEdit.setText(bundle.getString("note"));

            //Set transaction position for data submission
            transactionPosition = bundle.getInt("position");
        }

        //Button to delete data in editing mode
        if (editing) {
            deleteButton = findViewById(R.id.delete_button);
            //Make delete button visible
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> {
                deleteRecord(transactionPosition);
                Intent i = new Intent(AddTransaction.this, TransactionsOverview.class);
                startActivity(i);
            });
        }

        //Button to submit data and write to file
        submitButton = findViewById(R.id.add_transaction_button);
        if (editing) {
            submitButton.setText("Done");
        }
        submitButton.setOnClickListener(v -> {

            //Validate
            checksPassed = checkFields();
            if (checksPassed) {

                String color = "";

                //TODO: make this interact with the envelopes values?
                if (env_spinner.getSelectedItem().toString().equals("Groceries")) {
                    color = "#72cde1";
                } else if (env_spinner.getSelectedItem().toString().equals("Transportation")) {
                    color = "#e89d52";
                }

                //Build output
                @SuppressLint("DefaultLocale") String amount = String.format("%.2f", Double.parseDouble(amountEdit.getText().toString()));

                if(!incomeCheck.isChecked()) {
                    amount = "-" + amount;
                }

                String filename = "myTransactions.csv";
                String fileContents = String.join(
                        ",",
                        amount,
                        datePickerButton.getText().toString(),
                        account_spinner.getSelectedItem().toString(),
                        noteEdit.getText().toString(),
                        color,
                        env_spinner.getSelectedItem().toString()
                ) + "\n";

                if (editing) {
                    deleteRecord(transactionPosition);
                }

                //Write to file
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(AddTransaction.this, TransactionsOverview.class);
                startActivity(i);
            }
        });
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private void openDatePicker(View v) {
        datePickerDialog.show();
    }

    //Method to initialize date picker values
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            datePickerButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day +  " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default is never used
        return "JAN";
    }


    private void deleteRecord(int transactionPosition) {
        transactionPosition += 1;
        FileOutputStream outputStream;

        File dir = getFilesDir();
        File oldFile = new File(dir,"myTransactions.csv");

        int line = 0;
        String currentLine;

        String fileContents = "";

        try {
            FileInputStream fis = openFileInput("myTransactions.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            while((currentLine = br.readLine()) != null) {
                line++;

                if(transactionPosition != line) {
                    fileContents += (currentLine + "\n");
                }
            }

            oldFile.delete();

            outputStream = openFileOutput("myTransactions.csv", Context.MODE_APPEND);
            outputStream.write(fileContents.getBytes());
            outputStream.close();

            fis.close();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkFields() {
        datePickerButton.setError(null);
        amountEdit.setError(null);

        int falseCount = 0;

        boolean[] checks = new boolean[] {
                datePickerButton.length() > 0,
                amountEdit.length() > 0,
        };

        for (boolean check: checks) {
            if (!check) {
                falseCount++;
            }
        }

        for (int i = 0; i < checks.length; i++) {

            if(falseCount > 1) {
                Toast.makeText(this, "Please fill the required fields", Toast.LENGTH_SHORT).show();

                datePickerButton.setError("This field is required");
                amountEdit.setError("This field is required");

                return false;
            }

            if (!checks[i]) {

                switch (i) {
                    case 0:
                        datePickerButton.setError("This field is required");
                        Toast.makeText(this, "Date is required", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        amountEdit.setError("This field is required");
                        Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        }

        return true;

    }
}
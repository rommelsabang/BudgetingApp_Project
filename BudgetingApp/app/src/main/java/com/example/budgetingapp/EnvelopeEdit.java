package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

import com.example.budgetingapp.databinding.ActivityEnvelopeEditBinding;

public class EnvelopeEdit extends DrawerBaseActivity {

    ActivityEnvelopeEditBinding activityEnvelopeEditBinding;

    int envelopeColor = 0xFFFFFFFF;
    Button colorPickerButton;
    Button confirmButton;
    Button backButton;
    EditText nameText;
    EditText budgetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEnvelopeEditBinding = ActivityEnvelopeEditBinding.inflate(getLayoutInflater());
        setContentView(activityEnvelopeEditBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit an Envelope");

        allocateActivityTitle("Envelope Edit");

        setContentView(R.layout.activity_envelope_edit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String name = bundle.getString("name");
        String budget = bundle.getString("budget");
        String color = bundle.getString("color");

        nameText = (EditText) findViewById(R.id.editNameText);
        nameText.setText(name);
        budgetText = (EditText) findViewById(R.id.editBudgetText);
        budgetText.setText(budget);
        envelopeColor = Integer.parseUnsignedInt(color,16);

        colorPickerButton = (Button) findViewById(R.id.colorPickerButton);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEnvelope(name);
                Intent intent = new Intent(EnvelopeEdit.this, EnvelopesOverview.class);
                startActivity(intent);
            }
        });

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // Using open source color picker to pick hex color
    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, envelopeColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                envelopeColor = color;
                System.out.println(Integer.toUnsignedString(envelopeColor,16));
                System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "BudgetingApp" + "/" + "data.xml");
            }
        });
        colorPicker.show();
    }

    //TODO: Write method that takes in name, budget, and color and adds envelope to XML data file
    private void editEnvelope(String name) {
        StringBuffer buffer = new StringBuffer();
        if(isNumeric(budgetText.getText().toString())) {
            try {
                String line = "";
                FileInputStream fis = null;
                fis = openFileInput("envelopes.csv");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    if (line.contains(name)) {
                        line = nameText.getText() + "," + budgetText.getText() + "," + Integer.toUnsignedString(envelopeColor, 16);
                    }
                    buffer.append(line + "\n");
                }

                FileOutputStream outputStream = openFileOutput("envelopes.csv", 0);
                outputStream.write(buffer.toString().getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Budget must be a number", Toast.LENGTH_SHORT).show();
        }



    }

    public boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            double num = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.budgetingapp.databinding.ActivityEnvelopesOverviewBinding;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnvelopesOverview extends DrawerBaseActivity implements AdapterView.OnItemClickListener {

    ActivityEnvelopesOverviewBinding activityEnvelopesOverviewBinding;

    ListView lvEnvelope;
    ArrayList<String> envelopes = new ArrayList<String>();
    ImageView editImg;
    ImageView deleteImg;
    Button addEnvelopeButton;
    Button showReportsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEnvelopesOverviewBinding = ActivityEnvelopesOverviewBinding.inflate(getLayoutInflater());
        setContentView(activityEnvelopesOverviewBinding.getRoot());

        allocateActivityTitle("Envelopes Overview");

        setContentView(R.layout.activity_envelopes_overview);
        readEnvelopes();

        lvEnvelope = findViewById(R.id.lvEnvelope);
        ArrayAdapter<String> envelopeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, envelopes);
        lvEnvelope.setAdapter(new MyListAdapter(this, R.layout.list_item, envelopes));

        showReportsButton = findViewById(R.id.envelopeReportButton);
        showReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnvelopesOverview.this, reports.class);
                startActivity(intent);
            }
        });
        addEnvelopeButton = findViewById(R.id.addEnvelopeButton);
        addEnvelopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnvelopesOverview.this, EnvelopeAdd.class);
                startActivity(intent);
            }
        });


    }

    // Testing function to generate predetermined envelopes
    private void generateListContent() {
        envelopes.add("Recurring,1500,FF800000");
        envelopes.add("Grocery,500,FF00FF00");
        envelopes.add("Transportation,150,FF008080");
        envelopes.add("Subscriptions,100,FFFF0000");
        envelopes.add("Misc,550,FF808080");
    }

    private void readEnvelopes() {
        try {
            String line = "";
            FileInputStream fis = null;
            fis = openFileInput("envelopes.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                envelopes.add(line);
            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String envelope = parent.getItemAtPosition(position).toString();
//        Toast.makeText(getApplicationContext(), "Clicked: " + envelope, Toast.LENGTH_SHORT).show();
    }

    //Create custom list adapter to fit custom list items in ListView
    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            String[] contents = getItem(position).split(",");

            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.envelopeName);
                viewHolder.budget = (TextView) convertView.findViewById(R.id.envelopeBudget);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.envelopeAmount);
                viewHolder.edit = (ImageView) convertView.findViewById(R.id.editImg);
                viewHolder.delete = (ImageView) convertView.findViewById(R.id.deleteImg);

                convertView.setBackgroundColor(Integer.parseUnsignedInt(contents[2],16)); // Set background color
                convertView.setTag(viewHolder);

            }


            mainViewHolder = (ViewHolder) convertView.getTag();

            // Set contents of name and budget
            mainViewHolder.name.setText(contents[0]);
            mainViewHolder.budget.setText(contents[1]);
            mainViewHolder.amount.setText(String.format("%.2f",getEnvelopeAmount(contents[0])));
            mainViewHolder.color = contents[2];


            //TODO: Enter envelope editing activity after being clicked
            ViewHolder finalMainViewHolder = mainViewHolder;
            mainViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Edit button was clicked for list item " + position, Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(EnvelopesOverview.this, EnvelopeEdit.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("name", (String) finalMainViewHolder.name.getText());
                    bundle.putString("budget", (String) finalMainViewHolder.budget.getText());
                    bundle.putString("color", (String) finalMainViewHolder.color);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            //TODO: Create popup to delete row once clicked
            mainViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Delete button was clicked for list item " + position, Toast.LENGTH_SHORT).show();

                    showDeleteDialog((String) finalMainViewHolder.name.getText());
                }
            });

            return convertView;
        }
    }

    private double getEnvelopeAmount(String name) {
        double total = 0;
        try {
            String line = "";
            FileInputStream fis = null;
            fis = openFileInput("myTransactions.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if(line.contains(name)) {
                    String[] values = line.split(",");
                    total += Double.parseDouble(values[0]);
                }
            }
            //fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // awful way of removing -0.00 from the interface
        if(total == 0)
            return (total);
        else
            return (-total);
    }

    void showDeleteDialog(String name) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        Button yesButton = dialog.findViewById(R.id.yesButton);
        Button noButton = dialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEnvelope(name);
                recreate();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteEnvelope(String name) {
        StringBuffer buffer = new StringBuffer();

        try {
            String line = "";
            FileInputStream fis = null;
            fis = openFileInput("envelopes.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if(!line.contains(name)) {
                    buffer.append(line + "\n");
                }
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

    // List item contents
    public static class ViewHolder {
        ImageView edit;
        ImageView delete;
        TextView name;
        TextView budget;
        TextView amount;
        String color;

    }


}


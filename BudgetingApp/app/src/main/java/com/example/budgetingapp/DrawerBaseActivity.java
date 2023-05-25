package com.example.budgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                Intent intent = new Intent(this,Dashboard.class);
                overridePendingTransition(0, 0);
                startActivity(intent);
                break;
            case R.id.nav_transaction:
                Intent intent1 = new Intent(this,TransactionsOverview.class);
                overridePendingTransition(0, 0);
                startActivity(intent1);
                break;
            case R.id.nav_envelopes:
                Intent intent2 = new Intent(this,EnvelopesOverview.class);
                overridePendingTransition(0, 0);
                startActivity(intent2);
                break;
            case R.id.nav_account:
                Intent intent3 = new Intent(this,Accounts.class);
                overridePendingTransition(0, 0);
                startActivity(intent3);
                break;
            case R.id.nav_report:
                Intent intent4 = new Intent(this, reports.class);
                overridePendingTransition(0, 0);
                startActivity(intent4);
                break;
//            case R.id.nav_setting:
//                Intent intent5 = new Intent(this, Setting.class);
//                startActivity(intent5);
//                break;
        }
        return true;
    }

    protected void allocateActivityTitle(String titleString) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}

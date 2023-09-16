package com.example.minor;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout linearLayout;
    private CardView ploughCardView;
    private CardView sprayerCardView;
    private CardView seedDrillerCardView;
    private CardView thresherCardView;
    private CardView cultivatorCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navbar);

        // Set up the ActionBar and enable the menu icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up the ActionBarDrawerToggle to enable the toggle icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set the click listener for navigation items
        navigationView.setNavigationItemSelectedListener(this);

        linearLayout = findViewById(R.id.linearLayout);
        CardView tractorCardView = findViewById(R.id.tractor_card_view);
        ploughCardView = findViewById(R.id.plough_card_view);
        sprayerCardView = findViewById(R.id.sprayer_card_view);
        seedDrillerCardView = findViewById(R.id.seed_driller_card_view);
        thresherCardView = findViewById(R.id.thresher_card_view);
        cultivatorCardView = findViewById(R.id.cultivator_card_view);

        // Set click listeners for each card view
        tractorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage("Tractor");
            }
        });

        ploughCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage("Plough");
            }
        });

        sprayerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage("Sprayer");
            }
        });

        seedDrillerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage("Seed Driller");
            }
        });

        thresherCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage("Thresher");
            }
        });

        cultivatorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage("Cultivator");
            }
        });
    }

    private void openCategoryPage(String category) {
        Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        // You can perform specific actions based on the selected item
        // For example, launch a new activity or update the UI
        // You can add your own logic here
        int id = item.getItemId();

        if (id == R.id.optProfile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        // Close the drawer after handling the item click
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Toggle the navigation drawer when the menu icon is clicked
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Close the drawer if it's open, instead of closing the activity
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}



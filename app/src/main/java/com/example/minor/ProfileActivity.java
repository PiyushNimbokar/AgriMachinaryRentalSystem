package com.example.minor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button myItemsButton = findViewById(R.id.btnmyitems);
        myItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MyItemsActivity
                Intent intent = new Intent(ProfileActivity.this, MyItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}


package com.example.minor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageViewProduct;
    private TextView textViewProductName;
    private TextView textViewDuration;
    private TextView textViewDescription;
    private TextView textViewOwnerName;
    private TextView textViewPrice;
    private Button hireButton;

    private String userId;

    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Retrieve the data passed from the previous activity
        String productName = getIntent().getStringExtra("productName");
        String rentalDuration = getIntent().getStringExtra("rentalDuration");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");
        userId = getIntent().getStringExtra("userId");
        itemId = getIntent().getStringExtra("itemId");

        if (itemId != null) {
            // The userId is not null, you can use it here
            Log.i("DetailsActivity", "Item ID: " + itemId);
        } else {
            // The userId is null
            Log.e("DetailsActivity", "Item ID is null");
        }

        // Initialize the views
        imageViewProduct = findViewById(R.id.imageViewProduct);
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewDuration = findViewById(R.id.Duration);
        textViewDescription = findViewById(R.id.detailDesc);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewOwnerName = findViewById(R.id.ownerName);
        hireButton = findViewById(R.id.buttonHire);

        hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToConfirmationActivity();
            }
        });

        // Get a reference to the Firebase Realtime Database for the user
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ownerName = dataSnapshot.child("name").getValue(String.class);
                textViewOwnerName.setText(ownerName);
                Log.i(ownerName, "onDataChange:////////////////////////////////// ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        // Set the data to the views
        textViewProductName.setText(productName);
        textViewDuration.setText(rentalDuration);
        textViewDescription.setText(description);
        textViewPrice.setText(price);

        // Load the image using Glide library
        String imageUrl = getIntent().getStringExtra("imageUrl");
        Glide.with(this)
                .load(imageUrl)
                .into(imageViewProduct);
    }

    private void navigateToConfirmationActivity() {
        Intent intent = new Intent(DetailsActivity.this, ConfirmationActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
    }
}

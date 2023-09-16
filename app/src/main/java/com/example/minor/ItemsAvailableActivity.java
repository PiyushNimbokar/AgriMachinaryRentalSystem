package com.example.minor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemsAvailableActivity extends AppCompatActivity implements ItemAvailableAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ItemAvailableAdapter adapter;
    private List<ItemAvailable> itemList;
    private DatabaseReference databaseRef;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_available);

        recyclerView = findViewById(R.id.p_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        itemList = new ArrayList<>();
        adapter = new ItemAvailableAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Retrieve the category passed from the previous activity
        category = getIntent().getStringExtra("category");
        Log.d(category, "onCreate: //////////////////////////////////////////");

        // Initialize the database reference
        databaseRef = FirebaseDatabase.getInstance().getReference().child("AvailableItems");

        // Set the click listener for the adapter
        adapter.setOnItemClickListener(this);

        // Query the database for items with matching category and availability = "true"
        Query query = databaseRef.orderByChild("Category").equalTo(category);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemId = snapshot.getKey(); // Retrieve the item ID
                    ItemAvailable item = snapshot.getValue(ItemAvailable.class);
                    if (item != null && item.getAvailability()) {
                        item.setItemId(itemId); // Set the item ID in the ItemAvailable object
                        itemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerView.startAnimation(AnimationUtils.loadAnimation(ItemsAvailableActivity.this, R.anim.slide_in_right));
                Log.d(String.valueOf(itemList.size()), "onDataChange://///////////////");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemsAvailableActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(ItemAvailable item) {
        // Create an intent to start the DetailsActivity
        Intent intent = new Intent(ItemsAvailableActivity.this, DetailsActivity.class);

        // Pass the necessary data to the DetailsActivity
        intent.putExtra("productName", item.getName());
        intent.putExtra("rentalDuration", item.getDeadline());
        intent.putExtra("description", item.getCity());
        intent.putExtra("modelYear", item.getModelYear());
        intent.putExtra("price", item.getRentCost());
        intent.putExtra("imageUrl", item.getImageUrl());
        intent.putExtra("userId", item.getUserId());
        intent.putExtra("itemId", item.getItemId());

        Log.i(item.getUserId(), "onItemClick:////////////////////// ");
        Log.i(item.getItemId(), "onItemClick:////////////////////// ");

        startActivity(intent);
    }
}

package com.example.minor;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyItemsActivity extends AppCompatActivity implements MyItemsAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MyItemsAdapter myItemsAdapter;
    private List<MyItem> myItemsList;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        recyclerView = findViewById(R.id.myItemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myItemsList = new ArrayList<>();
        myItemsAdapter = new MyItemsAdapter(myItemsList);
        recyclerView.setAdapter(myItemsAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not logged in, handle accordingly
            return;
        }

        // Update the database reference path to point directly to "MyItems" under the user's node
        databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(currentUser.getUid())
                .child("MyItems");

        Log.i("MyItemsActivity", "Database Reference: " + databaseRef.toString());

        myItemsAdapter.setOnItemClickListener(this);

        fetchMyItemsData();
    }

    private void fetchMyItemsData() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myItemsList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String itemId = itemSnapshot.getKey();
                    Log.i("MyItemsActivity", "Item ID: " + itemId);

                    DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference()
                            .child("AvailableItems")
                            .child(itemId);

                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                MyItem itemDetails = dataSnapshot.getValue(MyItem.class);
                                if (itemDetails != null) {
                                    MyItem myItem = new MyItem(itemDetails.getName(), itemDetails.getDeadline(), itemDetails.getImageUrl());
                                    myItemsList.add(myItem);
                                    Log.i("MyItemsActivity", "Item added: " + myItem.getName());
                                    myItemsAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MyItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(MyItem myItem) {
        // Handle item click event if needed
    }
}


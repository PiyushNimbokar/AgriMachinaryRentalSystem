package com.example.minor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {

    private LinearLayout addressDetailsLayout;
    private TextView addressDetailsHeading;
    private boolean isAddressDetailsExpanded = false;

    private TextInputEditText addressEditText;
    private TextInputEditText pincodeEditText;
    private TextInputEditText cityEditText;

    private LinearLayout paymentDetailsLayout;
    private TextView paymentDetailsHeading;
    private boolean isPaymentDetailsExpanded = false;

    private TextInputEditText dateEditText;
    private Calendar calendar;

    private DatabaseReference itemsOnRentRef;

    private DatabaseReference databaseRef;

    private String userId;
    private String itemId;
    private String rentalDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        addressDetailsLayout = findViewById(R.id.addressDetailsLayout);
        addressDetailsHeading = findViewById(R.id.addressDetailsHeading);
        addressEditText = findViewById(R.id.addressEditText);
        pincodeEditText = findViewById(R.id.pincodeEditText);
        cityEditText = findViewById(R.id.cityEditText);

        addressDetailsHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAddressDetails();
            }
        });

        paymentDetailsLayout = findViewById(R.id.paymentDetailsLayout);
        paymentDetailsHeading = findViewById(R.id.paymentDetailsHeading);

        paymentDetailsHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePaymentDetails();
            }
        });

        dateEditText = findViewById(R.id.dateEditText);
        calendar = Calendar.getInstance();

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initialize Firebase database reference
        itemsOnRentRef = FirebaseDatabase.getInstance().getReference().child("ItemsOnRent");

        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Retrieve intent extras
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        itemId = intent.getStringExtra("itemId");
        rentalDeadline = intent.getStringExtra("rentalDeadline");
    }

    private void toggleAddressDetails() {
        if (isAddressDetailsExpanded) {
            addressDetailsLayout.setVisibility(View.GONE);
            addressDetailsHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_arrow_drop_down_24, 0);
        } else {
            addressDetailsLayout.setVisibility(View.VISIBLE);
            addressDetailsHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_arrow_drop_up_24, 0);
        }
        isAddressDetailsExpanded = !isAddressDetailsExpanded;
    }

    private void togglePaymentDetails() {
        if (isPaymentDetailsExpanded) {
            paymentDetailsLayout.setVisibility(View.GONE);
            paymentDetailsHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_arrow_drop_down_24, 0);
        } else {
            paymentDetailsLayout.setVisibility(View.VISIBLE);
            paymentDetailsHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_arrow_drop_up_24, 0);
        }
        isPaymentDetailsExpanded = !isPaymentDetailsExpanded;
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateEditText();
            }
        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateEditText() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateEditText.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void confirmButtonClicked(View view) {
        // Retrieve input values
        String address = addressEditText.getText().toString().trim();
        String pincode = pincodeEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String selectedDate = dateEditText.getText().toString().trim();

        // Validate input values
        if (address.isEmpty() || pincode.isEmpty() || city.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create RentDetails object
        RentDetails rentDetails = new RentDetails(userId, itemId, rentalDeadline, address, pincode, city, selectedDate);

        // Update item availability in Firebase database
        databaseRef.child("AvailableItems").child(itemId).child("Availability").setValue(false);

        // Add rent details to Firebase database
        DatabaseReference itemsOnRentRef = FirebaseDatabase.getInstance().getReference().child("ItemsOnRent");
        String rentId = itemsOnRentRef.push().getKey();
        itemsOnRentRef.child(rentId).setValue(rentDetails)
                .addOnSuccessListener(aVoid -> {
                    // Rent details added successfully to ItemsOnRent

                    // Add rented item ID to MyOrders under the user's node
                    DatabaseReference myOrdersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("MyOrders");
                    myOrdersRef.child("rentId").setValue(rentId)
                            .addOnSuccessListener(aVoid1 -> {
                                // Item ID added to MyOrders successfully
                                Toast.makeText(getApplicationContext(), "Confirmation details added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                // Failed to add item ID to MyOrders
                                Toast.makeText(getApplicationContext(), "Failed to add item to MyOrders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            });
                })
                .addOnFailureListener(e -> {
                    // Failed to add rent details to ItemsOnRent
                    Toast.makeText(getApplicationContext(), "Failed to add confirmation details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

}

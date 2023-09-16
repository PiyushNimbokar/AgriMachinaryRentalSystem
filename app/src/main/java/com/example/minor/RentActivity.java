package com.example.minor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RentActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private String userId;
    private TextInputEditText uploadNameEditText;
    private TextInputEditText cityEditText;
    private TextInputEditText modelYearEditText;
    private TextInputEditText rentCostEditText;
    private TextInputEditText deadlineEditText;
    private Button submitButton;
    private ImageView productImageView;
    private ImageView previewImageView;
    private Uri imageUri;
    private boolean isImageSelected = false;
    private SimpleDateFormat dateFormatter;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        // Get a reference to the Firebase Realtime Database
        databaseRef = FirebaseDatabase.getInstance().getReference();
        // Get a reference to the Firebase Storage
        storageRef = FirebaseStorage.getInstance().getReference();
        // Get the current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get references to the form fields
        uploadNameEditText = findViewById(R.id.nameform);
        cityEditText = findViewById(R.id.cityform);
        modelYearEditText = findViewById(R.id.modelYearform);
        rentCostEditText = findViewById(R.id.rentCostform);
        deadlineEditText = findViewById(R.id.deadlineform);
        submitButton = findViewById(R.id.submitButton);
        productImageView = findViewById(R.id.productImageView);
        previewImageView = findViewById(R.id.previewImageView);

        // Set an onClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        // Set an onClickListener for the product image view
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermissions();
            }
        });

        // Initialize date formatter
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        // Set an onClickListener for the deadline edit text
        deadlineEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void submitForm() {
        String itemName = uploadNameEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String modelYear = modelYearEditText.getText().toString().trim();
        String rentCost = rentCostEditText.getText().toString().trim();
        String deadline = deadlineEditText.getText().toString().trim();

        if (itemName.isEmpty() || city.isEmpty() || modelYear.isEmpty() || rentCost.isEmpty() || deadline.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isImageSelected) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = getIntent().getStringExtra("category");

        // Generate a unique ID for the item
        String itemId = databaseRef.child("Items").child(category).push().getKey();


        // Create a HashMap to hold the item details
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("Category", category);
        itemData.put("Name", itemName);
        itemData.put("City", city);
        itemData.put("ModelYear", modelYear);
        itemData.put("RentCost", rentCost);
        itemData.put("Deadline", deadline);
        itemData.put("Availability", true);
        itemData.put("UserId", userId);

        // Save the item details to the "AvailableItems" list under the corresponding category and item ID
        databaseRef.child("AvailableItems").child(itemId).setValue(itemData)
                .addOnSuccessListener(aVoid -> {
                    // Item details saved successfully to "AvailableItems" list
                    databaseRef.child("Users").child(userId).child("MyItems").child("ItemId").setValue(itemId);
                    uploadImageToStorage(itemId);
                })
                .addOnFailureListener(e -> {
                    // Failed to save item details to "AvailableItems" list
                    Toast.makeText(getApplicationContext(), "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImageToStorage(String itemId) {
        StorageReference imageRef = storageRef.child("item_images").child(itemId + ".jpg");
        UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully
            Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            // Get the URL of the uploaded image
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Save the image URL to the item details in the database
                String imageUrl = uri.toString();
                databaseRef.child("AvailableItems").child(itemId).child("ImageUrl").setValue(imageUrl)
                        .addOnSuccessListener(aVoid -> {
                            // Image URL saved successfully
                            Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                            // Navigate back to the home screen
                            onBackPressed();
                        })
                        .addOnFailureListener(e -> {
                            // Failed to save image URL
                            Toast.makeText(getApplicationContext(), "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            // Failed to upload image
            Toast.makeText(getApplicationContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String selectedDateString = dateFormatter.format(selectedDate.getTime());
                        deadlineEditText.setText(selectedDateString);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                launchGalleryIntent();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, STORAGE_PERMISSION_REQUEST_CODE);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchGalleryIntent();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchGalleryIntent();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            previewImageView.setImageURI(imageUri);
            isImageSelected = true;
        }
    }
}

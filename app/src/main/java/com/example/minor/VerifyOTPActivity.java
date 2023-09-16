package com.example.minor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class VerifyOTPActivity extends AppCompatActivity {

    private EditText editTextOTP;
    private Button btnVerifyOTP;

    private FirebaseAuth firebaseAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        editTextOTP = findViewById(R.id.editTextOTP);
        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);

        firebaseAuth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editTextOTP.getText().toString().trim();
                if (!otp.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "Please enter OTP", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // OTP verification successful, proceed to the next activity or store data in the database
                        Toast.makeText(VerifyOTPActivity.this, "OTP verification successful", Toast.LENGTH_LONG).show();
                        // Get user details
                        String name = getIntent().getStringExtra("name");
                        String mobileNumber = getIntent().getStringExtra("mobileNumber");
                        // Store the user details in the database
                        saveUserData(name, mobileNumber);

                        // Example: Go to MainActivity
                        startActivity(new Intent(VerifyOTPActivity.this, MainActivity.class));
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(VerifyOTPActivity.this, "Invalid OTP", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(VerifyOTPActivity.this, "OTP verification failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveUserData(String name, String mobileNumber) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        User user = new User(name, mobileNumber);
        usersRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully saved to the database
                    Toast.makeText(VerifyOTPActivity.this, "User data saved successfully", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save data to the database
                    Toast.makeText(VerifyOTPActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}

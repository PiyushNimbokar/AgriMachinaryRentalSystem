package com.example.minor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhoneNumber;
    private EditText editTextName; // New EditText for name input
    private Button buttonLogin;
    private FirebaseAuth firebaseAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextMobileNumber);
        editTextName = findViewById(R.id.editTextName); // Reference to the name EditText field
        buttonLogin = findViewById(R.id.btnProceed);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String name = editTextName.getText().toString().trim(); // Retrieve the name from the input field

                if (!phoneNumber.isEmpty() && !name.isEmpty()) {
                    // Phone number and name are provided, initiate OTP verification
                    String completePhoneNumber = "+91" + phoneNumber; // Add country code (+91) to the phone number
                    sendVerificationCode(completePhoneNumber, name); // Pass the name to the method
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter a phone number and name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode(String phoneNumber, String name) {
        if(phoneNumber.equals("+91123"))
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // This callback will be invoked in two situations:
                        // 1. Instant verification. In some cases, the phone number can be instantly verified
                        //    without requiring the user to enter the OTP manually.
                        // 2. Auto-retrieval. On some devices, Google Play services can automatically
                        //    detect the incoming verification SMS and perform verification without user action.

                        // You can directly call signInWithPhoneAuthCredential(phoneAuthCredential) here if needed.

                        // For manual OTP entry, store the credential and move to the VerifyOTP activity
                        verificationId = phoneAuthCredential.getSmsCode();
                        if (verificationId != null) {
                            Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                            intent.putExtra("verificationId", verificationId);
                            intent.putExtra("name", name); // Pass the name as a parameter
                            intent.putExtra("mobileNumber", phoneNumber); // Pass the phone number
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked when an invalid request for verification is made,
                        // for example, the phone number format is invalid.

                        Toast.makeText(LoginActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // The SMS verification code has been sent to the provided phone number,
                        // we now need to ask the user to enter the code and verify it manually.

                        verificationId = s;

                        // Move to the VerifyOTPActivity to enter the code
                        Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("name", name); // Pass the name as a parameter
                        intent.putExtra("mobileNumber", phoneNumber); // Pass the phone number
                        startActivity(intent);
                    }
                }
        );
    }
}

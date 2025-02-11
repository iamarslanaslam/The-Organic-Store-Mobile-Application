package com.example.theorganicstoremobileapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.theorganicstoremobileapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
// other imports

public class RegistrationActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etFirstName, etLastName, etGender, etAddress, etContactNo;
    private Spinner spinnerRole;
    private Button btnRegister;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private boolean isEditMode = false;
    private String userId;

    boolean doAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etGender = findViewById(R.id.etGender);
        etAddress = findViewById(R.id.etAddress);
        etContactNo = findViewById(R.id.etContactNo);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        // Populate the Spinner with roles
        populateSpinner();

        // Check if this is edit mode
        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            isEditMode = true;
            userId = intent.getStringExtra("userId");
            loadUserData(userId);
            btnRegister.setText("Update");
        }

        if (intent.hasExtra("DoAdd")) {
            doAdd = intent.getBooleanExtra("DoAdd", false);
        }


        btnRegister.setOnClickListener(v -> registerOrUpdateUser());


    }

    private void populateSpinner() {
        List<String> roles = new ArrayList<>();
        roles.add("User");
        roles.add("Moderator");
        roles.add("Admin");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private void loadUserData(String userId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading user data...");
        progressDialog.show();
        // Fetch user data from Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            etEmail.setText(user.getEmail());
                            etPassword.setText(user.getPassword()); // Password field can remain empty for security reasons
                            etFirstName.setText(user.getFirstName());
                            etLastName.setText(user.getLastName());
                            etGender.setText(user.getGender());
                            etAddress.setText(user.getAddress());
                            etContactNo.setText(user.getContactNo());
                            int rolePosition = ((ArrayAdapter<String>) spinnerRole.getAdapter()).getPosition(user.getRole());
                            spinnerRole.setSelection(rolePosition);

                            // Disable email field if editing
                            etEmail.setEnabled(false);
                            etPassword.setEnabled(false);
                        }
                    }
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show());
    }

    private void registerOrUpdateUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String contactNo = etContactNo.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();
        String dateOfRegistration = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (validateInputs(email, password, firstName, lastName)) {
            progressDialog.show();

            if (isEditMode) {
                // Update existing user data in Firestore

                User user = new User(userId, email, password, firstName, lastName, gender, address, contactNo, dateOfRegistration, true, "Courier X", "Credit Card", role);
                db.collection("users").document(userId).set(user)
                        .addOnSuccessListener(aVoid -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Register a new user with Firebase Auth
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                userId = firebaseUser != null ? firebaseUser.getUid() : null;

                                User user = new User(userId, email, password, firstName, lastName, gender, address, contactNo, dateOfRegistration, true, "Courier X", "Credit Card", role);
                                db.collection("users").document(userId).set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            if (doAdd) {
                                                Toast.makeText(this, "Added Successfully successful", Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private boolean validateInputs(String email, String password, String firstName, String lastName) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            return false;
        }
        if (!isEditMode && (password.isEmpty() || password.length() < 6)) { // Only validate password if registering
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        if (firstName.isEmpty()) {
            etFirstName.setError("First name required");
            return false;
        }
        if (lastName.isEmpty()) {
            etLastName.setError("Last name required");
            return false;
        }
        return true;
    }
}

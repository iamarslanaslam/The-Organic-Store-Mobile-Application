package com.example.theorganicstoremobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
//VDeclaration
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog; // Declare ProgressDialog
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Calls the superclass (Activity) onCreate method to perform the default activity creation tasks.
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false); // Prevent dismissal by user interaction

        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegistrationActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInputs(email, password)) {
            // Show ProgressDialog before starting the login process
            progressDialog.show();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        // Dismiss the ProgressDialog after login attempt
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Login successful, now check user role
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();
                                fetchUserRole(userId);
                            }
                        } else {
                            Log.d("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void fetchUserRole(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch user role
                        String role = documentSnapshot.getString("role");
                        handleUserRole(role);
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch user role", Toast.LENGTH_SHORT).show());
    }

    private void handleUserRole(String role) {
        if ("Admin".equals(role)) {
            // Redirect to Admin Activity
            Toast.makeText(this, "Admin Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminDashboard.class));
        } else if ("Moderator".equals(role)) {
            // Redirect to Moderator Activity
            Toast.makeText(this, "Moderator Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, all_orders.class));
        } else {
            // Redirect to User Activity
            Toast.makeText(this, "User Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ProductListActivity.class));
        }
        finish(); // Close the login activity
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password required");
            return false;
        }
        return true;
    }
}

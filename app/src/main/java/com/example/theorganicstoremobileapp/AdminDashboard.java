package com.example.theorganicstoremobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    private Button btnManageProducts, btnManageModerators, btnManageAnnouncements;
    private Button btnManagePayments, btnManageEmployees, btnManageUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       //basic setup
        setContentView(R.layout.activity_admin_dashboard);
        // Initialize buttons
        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnManageModerators = findViewById(R.id.btnManageModerators);
        btnManageAnnouncements = findViewById(R.id.btnManageAnnouncements);
        btnManagePayments = findViewById(R.id.btnManagePayments);
        btnManageEmployees = findViewById(R.id.btnManageEmployees);
        btnManageUsers = findViewById(R.id.btnManageUsers);

        // Set click listeners
        btnManageProducts.setOnClickListener(v -> startActivity(new Intent(this, AllProductsActivity.class)));
        btnManageModerators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminDashboard.this, AllUsersActivity.class);
                i.putExtra("userType", "moderator");
                startActivity(i);
            }
        });
        btnManageAnnouncements.setOnClickListener(v -> startActivity(new Intent(this, ManageAnnouncementsActivity.class)));
        btnManagePayments.setOnClickListener(v -> startActivity(new Intent(this, AllPaymentsActivity.class)));
        btnManageEmployees.setOnClickListener(v -> startActivity(new Intent(this, AllEmployees.class)));
        btnManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminDashboard.this, AllUsersActivity.class);
                i.putExtra("userType", "user");
                startActivity(i);
            }
        });


        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        });

    }
}
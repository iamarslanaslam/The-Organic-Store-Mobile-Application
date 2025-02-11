package com.example.theorganicstoremobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theorganicstoremobileapp.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class AddProductActivity extends AppCompatActivity {
    private EditText editTextName, editTextLastName, editTextDate, editTextPrice, editTextCategory, editTextSubcategory;
    private Button buttonAddProduct;
    private FirebaseFirestore db;
    private String productId; // Variable to hold the product ID for editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextSubcategory = findViewById(R.id.editTextSubcategory);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        db = FirebaseFirestore.getInstance();

        // Check if we are editing an existing product
        Intent intent = getIntent();
        if (intent.hasExtra("productId")) {
            productId = intent.getStringExtra("productId");
            String productName = intent.getStringExtra("productName");
            String productLastName = intent.getStringExtra("productLastName");
            String productDate = intent.getStringExtra("productDate");
            String productPrice = intent.getStringExtra("productPrice");
            String productCategory = intent.getStringExtra("productCategory");
            String productSubcategory = intent.getStringExtra("productSubcategory");

            // Populate the fields with the existing product data
            editTextName.setText(productName);
            editTextLastName.setText(productLastName);
            editTextDate.setText(productDate);
            editTextPrice.setText(productPrice);
            editTextCategory.setText(productCategory);
            editTextSubcategory.setText(productSubcategory);
            buttonAddProduct.setText("Update Product"); // Change button text for updating
        } else {
            productId = null; // No product ID means we are adding a new product
        }

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productId != null) {
                    updateProduct(); // Call update if productId exists
                } else {
                    addProduct(); // Call add if no productId
                }
            }
        });
    }

    private void addProduct() {
        String name = editTextName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String dateOfRegistration = editTextDate.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String subcategory = editTextSubcategory.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(dateOfRegistration) ||
                TextUtils.isEmpty(price) || TextUtils.isEmpty(category) || TextUtils.isEmpty(subcategory)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new product
        String id = db.collection("products").document().getId();
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setLastName(lastName);
        product.setDateOfRegistration(dateOfRegistration);
        product.setPrice(price);
        product.setCategory(category);
        product.setSubcategory(subcategory);

        // Add product to Firestore
        db.collection("products").document(id)
                .set(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally, navigate back
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddProductActivity.this, "Error adding product", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProduct() {
        String name = editTextName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String dateOfRegistration = editTextDate.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String subcategory = editTextSubcategory.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(dateOfRegistration) ||
                TextUtils.isEmpty(price) || TextUtils.isEmpty(category) || TextUtils.isEmpty(subcategory)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to the existing product document
        DocumentReference productRef = db.collection("products").document(productId);

        // Update the product in Firestore
        productRef.update("name", name, "lastName", lastName, "dateOfRegistration", dateOfRegistration,
                        "price", price, "category", category, "subcategory", subcategory)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally, navigate back
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddProductActivity.this, "Error updating product", Toast.LENGTH_SHORT).show();
                });
    }
}

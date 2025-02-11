package com.example.theorganicstoremobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.ProductAdapter;
import com.example.theorganicstoremobileapp.adapters.ProductAdapterUser;
import com.example.theorganicstoremobileapp.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapterUser adapter;
    private FirebaseFirestore db;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        db = FirebaseFirestore.getInstance();


//         Fetch products from Firestore
        loadProducts();

//         Floating button to go to the cart screen
        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts() {
        db.collection("products").get().addOnSuccessListener(querySnapshot -> {
            productList.clear();
            for (QueryDocumentSnapshot document : querySnapshot) {
                Product product = document.toObject(Product.class);
                productList.add(product);
            }
            adapter = new ProductAdapterUser(productList, this::addToCart);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show()
        );
    }

    private void addToCart(Product product) {
        // Add to cart logic (could store product in shared preferences, database, etc.)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid())
                .collection("cart")
                .add(product);

        Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }
}
package com.example.theorganicstoremobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.ProductAdapter;
import com.example.theorganicstoremobileapp.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;

    private FloatingActionButton fabAddProduct;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();


        db = FirebaseFirestore.getInstance();
        fetchProducts();

        fabAddProduct = findViewById(R.id.addProduct);
        fabAddProduct.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));
        searchView = findViewById(R.id.searchViewProducts);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return false;
            }
        });
    }

    private void searchProducts(String newText) {
        ArrayList<Product> searchList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                searchList.add(product);
            }
        }
        productAdapter.filterList(searchList);
    }

    private void fetchProducts() {
        db.collection("products") // Change "products" to your collection name
                .addSnapshotListener( // Real-time updates
                        (value, error) -> {
                            productList.clear();
                            if (error != null) {
                                return;
                            }

                            for (DocumentChange dc : value.getDocumentChanges()) {
                                Product product = dc.getDocument().toObject(Product.class);
                                productList.add(product);
                            }
                            productAdapter = new ProductAdapter(productList);
                            recyclerView.setAdapter(productAdapter);
                        }
                );
    }
}

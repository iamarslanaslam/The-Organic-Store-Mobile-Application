package com.example.theorganicstoremobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.PaymentAdapter;
import com.example.theorganicstoremobileapp.models.Payment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllPaymentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPayments;
    private PaymentAdapter paymentAdapter;
    private ArrayList<Payment> paymentList;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_payments);

        recyclerViewPayments = findViewById(R.id.recyclerViewPayments);
        recyclerViewPayments.setLayoutManager(new LinearLayoutManager(this));

        paymentList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentList);
        recyclerViewPayments.setAdapter(paymentAdapter);

        searchView = findViewById(R.id.searchViewPayments);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPayments(newText);
                return false;
            }
        });

        loadPayments();

        FloatingActionButton fab = findViewById(R.id.fabAddPayment);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPaymentActivity.class));
        });
    }

    private void searchPayments(String newText) {
        ArrayList<Payment> searchResults = new ArrayList<>();
        for (Payment payment : paymentList) {
            if (payment.getPaymentId().toLowerCase().contains(newText.toLowerCase())) {
                searchResults.add(payment);
            }
        }
        paymentAdapter.filterPayments(searchResults);
    }

    private void loadPayments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("payments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            paymentList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Payment payment = document.toObject(Payment.class);
                                paymentList.add(payment);
                            }
                            paymentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(this, "Failed to load payments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

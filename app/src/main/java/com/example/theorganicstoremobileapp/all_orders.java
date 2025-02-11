package com.example.theorganicstoremobileapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.OrderAdapter;
import com.example.theorganicstoremobileapp.models.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class all_orders extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample order data
        orderList = new ArrayList<>();
        // Add some orders here or fetch from database

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            orderList.clear();
            for (int i = 0; i < value.size(); i++) {
                Order order = value.getDocuments().get(i).toObject(Order.class);
                if(!order.getStatus().equals("Delivered")){
                    orderList.add(order);
                }
            }

            adapter = new OrderAdapter(orderList, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });


    }





    @Override
    public void onConfirmOrder(Order order) {
        // Handle order confirmation logic
        order.setStatus("Confirmed");
        //save in firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(order.getId()).update("status", "Confirmed");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeliverOrder(Order order) {
        // Handle order delivery logic
        order.setStatus("Delivered");
        //save in firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(order.getId()).update("status", "Confirmed");
        adapter.notifyDataSetChanged();
    }
}
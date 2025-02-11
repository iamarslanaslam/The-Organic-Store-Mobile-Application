package com.example.theorganicstoremobileapp.models;

import java.util.List;

public class Order {
    private  String id;
    private String userId;
    private List<Product> items;
    private String status;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String id, String userId, List<Product> items, String status) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.example.theorganicstoremobileapp.models;

public class Product {
    private String id;
    private String name;
    private String price;
    private String category;
    private String subcategory;
    private String dateOfRegistration;
    private String lastName;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String id, String name, String price, String category, String subcategory, String dateOfRegistration, String lastName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.subcategory = subcategory;
        this.dateOfRegistration = dateOfRegistration;
        this.lastName = lastName;
    }

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for price
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    // Getter and Setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getter and Setter for subcategory
    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    // Getter and Setter for dateOfRegistration
    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    // Getter and Setter for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

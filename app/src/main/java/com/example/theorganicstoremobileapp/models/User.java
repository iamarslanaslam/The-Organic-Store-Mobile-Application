package com.example.theorganicstoremobileapp.models;

public class User {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String contactNo;
    private String dateOfRegistration;
    private boolean canOrder;
    private String selectedCourier;
    private String paymentMode;
    private String role;


    public User() {
    }
    // Constructor


    public boolean isCanOrder() {
        return canOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String id, String email, String password, String firstName, String lastName, String gender, String address, String contactNo, String dateOfRegistration, boolean canOrder, String selectedCourier, String paymentMode, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.contactNo = contactNo;
        this.dateOfRegistration = dateOfRegistration;
        this.canOrder = canOrder;
        this.selectedCourier = selectedCourier;
        this.paymentMode = paymentMode;
        this.role = role;
    }

    // Getters and Setters for each field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public boolean canOrder() {
        return canOrder;
    }

    public void setCanOrder(boolean canOrder) {
        this.canOrder = canOrder;
    }

    public String getSelectedCourier() {
        return selectedCourier;
    }

    public void setSelectedCourier(String selectedCourier) {
        this.selectedCourier = selectedCourier;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

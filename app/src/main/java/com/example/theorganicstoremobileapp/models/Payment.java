package com.example.theorganicstoremobileapp.models;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    private String paymentId;
    private String payerId;
    private String receiverCompany;
    private String courier;
    private double amount;
    private String paymentMethod; // e.g., "Credit Card", "Cash", etc.
    private String status; // e.g., "Pending", "Completed", "Refunded"
    private Date paymentDate;
    private String notes;

    public Payment() {
    }

    // Constructor
    public Payment(String paymentId, String payerId, String receiverCompany, String courier, double amount,
                   String paymentMethod, String status, Date paymentDate, String notes) {
        this.paymentId = paymentId;
        this.payerId = payerId;
        this.receiverCompany = receiverCompany;
        this.courier = courier;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentDate = paymentDate;
        this.notes = notes;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getReceiverCompany() {
        return receiverCompany;
    }

    public void setReceiverCompany(String receiverCompany) {
        this.receiverCompany = receiverCompany;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

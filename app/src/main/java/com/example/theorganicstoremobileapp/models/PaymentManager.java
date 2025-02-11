package com.example.theorganicstoremobileapp.models;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentManager {

    private FirebaseFirestore db;

    public PaymentManager() {
        db = FirebaseFirestore.getInstance();
    }

    // Add Payment Record
    public void addPayment(Payment payment) {
        db.collection("payments").document(payment.getPaymentId())
                .set(payment)
                .addOnSuccessListener(aVoid -> System.out.println("Payment added successfully"))
                .addOnFailureListener(e -> System.err.println("Error adding payment: " + e.getMessage()));
    }

    // Modify Payment Record
    public void modifyPayment(String paymentId, Payment payment) {
        db.collection("payments").document(paymentId)
                .set(payment)
                .addOnSuccessListener(aVoid -> System.out.println("Payment modified successfully"))
                .addOnFailureListener(e -> System.err.println("Error modifying payment: " + e.getMessage()));
    }

    // Delete Payment Record
    public void deletePayment(String paymentId) {
        db.collection("payments").document(paymentId)
                .delete()
                .addOnSuccessListener(aVoid -> System.out.println("Payment deleted successfully"))
                .addOnFailureListener(e -> System.err.println("Error deleting payment: " + e.getMessage()));
    }

    // Search for a Payment (example by company name or courier)
    public void searchPayment(String field, String value) {
        db.collection("payments")
                .whereEqualTo(field, value)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.forEach(document -> System.out.println(document.toObject(Payment.class)));
                    } else {
                        System.out.println("No payment records found for search criteria.");
                    }
                })
                .addOnFailureListener(e -> System.err.println("Error searching payments: " + e.getMessage()));
    }
}

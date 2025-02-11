package com.example.theorganicstoremobileapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.theorganicstoremobileapp.models.Payment;
import com.example.theorganicstoremobileapp.models.PaymentManager;

import java.util.Date;

public class AddPaymentActivity extends AppCompatActivity {

    private EditText etPayerId, etReceiverCompany, etCourier, etAmount, etPaymentMethod, etStatus, etNotes;
    private Button btnAddOrUpdatePayment;
    private PaymentManager paymentManager;
    private Payment existingPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        // Initialize views
        etPayerId = findViewById(R.id.etPayerId);
        etReceiverCompany = findViewById(R.id.etReceiverCompany);
        etCourier = findViewById(R.id.etCourier);
        etAmount = findViewById(R.id.etAmount);
        etPaymentMethod = findViewById(R.id.etPaymentMethod);
        etStatus = findViewById(R.id.etStatus);
        etNotes = findViewById(R.id.etNotes);
        btnAddOrUpdatePayment = findViewById(R.id.btnAddPayment);

        // Initialize PaymentManager
        paymentManager = new PaymentManager();

        // Check if a payment object is passed for updating
        existingPayment = (Payment) getIntent().getSerializableExtra("payment");
        if (existingPayment != null) {
            setUpForEdit(existingPayment);
            btnAddOrUpdatePayment.setText("Update Payment");
        } else {
            btnAddOrUpdatePayment.setText("Add Payment");
        }

        // Set button click listener
        btnAddOrUpdatePayment.setOnClickListener(v -> {
            if (existingPayment != null) {
                updatePayment();
            } else {
                addPayment();
            }
        });
    }

    private void setUpForEdit(Payment payment) {
        etPayerId.setText(payment.getPayerId());
        etReceiverCompany.setText(payment.getReceiverCompany());
        etCourier.setText(payment.getCourier());
        etAmount.setText(String.valueOf(payment.getAmount()));
        etPaymentMethod.setText(payment.getPaymentMethod());
        etStatus.setText(payment.getStatus());
        etNotes.setText(payment.getNotes());
    }

    private void addPayment() {
        String payerId = etPayerId.getText().toString().trim();
        String receiverCompany = etReceiverCompany.getText().toString().trim();
        String courier = etCourier.getText().toString().trim();
        String amountString = etAmount.getText().toString().trim();
        String paymentMethod = etPaymentMethod.getText().toString().trim();
        String status = etStatus.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(payerId) || TextUtils.isEmpty(receiverCompany) || TextUtils.isEmpty(amountString)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);

        Payment payment = new Payment(
                String.valueOf(System.currentTimeMillis()),
                payerId,
                receiverCompany,
                courier,
                amount,
                paymentMethod,
                status,
                new Date(),
                notes
        );

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding payment...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        paymentManager.addPayment(payment);
        progressDialog.dismiss();
        Toast.makeText(this, "Payment added successfully", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void updatePayment() {
        String payerId = etPayerId.getText().toString().trim();
        String receiverCompany = etReceiverCompany.getText().toString().trim();
        String courier = etCourier.getText().toString().trim();
        String amountString = etAmount.getText().toString().trim();
        String paymentMethod = etPaymentMethod.getText().toString().trim();
        String status = etStatus.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(payerId) || TextUtils.isEmpty(receiverCompany) || TextUtils.isEmpty(amountString)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);

        existingPayment.setPayerId(payerId);
        existingPayment.setReceiverCompany(receiverCompany);
        existingPayment.setCourier(courier);
        existingPayment.setAmount(amount);
        existingPayment.setPaymentMethod(paymentMethod);
        existingPayment.setStatus(status);
        existingPayment.setNotes(notes);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating payment...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        paymentManager.modifyPayment(existingPayment.getPaymentId(), existingPayment);
        progressDialog.dismiss();
        Toast.makeText(this, "Payment updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void clearFields() {
        etPayerId.setText("");
        etReceiverCompany.setText("");
        etCourier.setText("");
        etAmount.setText("");
        etPaymentMethod.setText("");
        etStatus.setText("");
        etNotes.setText("");
    }
}

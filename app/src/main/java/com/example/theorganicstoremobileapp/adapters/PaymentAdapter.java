package com.example.theorganicstoremobileapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.AddPaymentActivity;
import com.example.theorganicstoremobileapp.R;
import com.example.theorganicstoremobileapp.models.Payment;
import com.example.theorganicstoremobileapp.models.PaymentManager;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private  ArrayList<Payment> paymentList;

    public PaymentAdapter(ArrayList<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.tvPaymentId.setText("Payment ID: " + payment.getPaymentId());
        holder.tvPayer.setText("Payer: " + payment.getPayerId());
        holder.tvReceiverCompany.setText("Receiver Company: " + payment.getReceiverCompany());
        holder.tvAmount.setText("Amount: $" + payment.getAmount());
        holder.tvStatus.setText("Status: " + payment.getStatus());

        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddPaymentActivity.class);
            intent.putExtra("payment", payment);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnDeleteProduct.setOnClickListener(v -> {
            PaymentManager manger = new PaymentManager();
            manger.deletePayment(payment.getPaymentId());
            paymentList.remove(payment);
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public void filterPayments(ArrayList<Payment> searchResults) {
        paymentList = searchResults;

        notifyDataSetChanged();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentId, tvPayer, tvReceiverCompany, tvAmount, tvStatus;
        Button btnEditProduct;
        Button btnDeleteProduct;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentId = itemView.findViewById(R.id.tvPaymentId);
            tvPayer = itemView.findViewById(R.id.tvPayer);
            tvReceiverCompany = itemView.findViewById(R.id.tvReceiverCompany);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEditProduct = itemView.findViewById(R.id.edit_payment);
            btnDeleteProduct = itemView.findViewById(R.id.delete_payment);
        }
    }
}

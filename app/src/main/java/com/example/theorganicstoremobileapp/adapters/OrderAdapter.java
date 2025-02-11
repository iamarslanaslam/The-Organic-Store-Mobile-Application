package com.example.theorganicstoremobileapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.theorganicstoremobileapp.R;
import com.example.theorganicstoremobileapp.models.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orders;
    private final OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onConfirmOrder(Order order);
        void onDeliverOrder(Order order);
    }

    public OrderAdapter(List<Order> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderId.setText("Order ID: " + order.getId());
        holder.orderStatus.setText("Status: " + order.getStatus());
        holder.orderItems.setText("Items: " + order.getItems().toString());


        if (order.getStatus().equals("Pending")) {
            holder.confirmOrderButton.setVisibility(View.VISIBLE);
            holder.deliverOrderButton.setVisibility(View.GONE);
        } else if (order.getStatus().equals("Confirmed")) {
            holder.confirmOrderButton.setVisibility(View.GONE);
            holder.deliverOrderButton.setVisibility(View.VISIBLE);
        } else {
            holder.confirmOrderButton.setVisibility(View.GONE);
            holder.deliverOrderButton.setVisibility(View.GONE);
        }

        holder.confirmOrderButton.setOnClickListener(v -> listener.onConfirmOrder(order));
        holder.deliverOrderButton.setOnClickListener(v -> listener.onDeliverOrder(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, orderItems;
        Button confirmOrderButton, deliverOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderItems = itemView.findViewById(R.id.orderItems);
            confirmOrderButton = itemView.findViewById(R.id.confirmOrderButton);
            deliverOrderButton = itemView.findViewById(R.id.deliverOrderButton);
        }
    }
}

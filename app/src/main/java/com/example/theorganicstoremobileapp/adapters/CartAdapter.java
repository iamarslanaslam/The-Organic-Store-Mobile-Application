package com.example.theorganicstoremobileapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.R;
import com.example.theorganicstoremobileapp.models.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> cartProducts;

    public CartAdapter(List<Product> cartProducts) {
        this.cartProducts = cartProducts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartProducts.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("Price: " + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewCartProductName);
            textViewPrice = itemView.findViewById(R.id.textViewCartProductPrice);
        }
    }
}

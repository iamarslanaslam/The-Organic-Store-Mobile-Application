package com.example.theorganicstoremobileapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.R;
import com.example.theorganicstoremobileapp.models.Product;

import java.util.List;

public class ProductAdapterUser extends RecyclerView.Adapter<ProductAdapterUser.ProductViewUserHolder> {

    private final List<Product> products;
    private final OnAddToCartListener listener;

    public ProductAdapterUser(List<Product> products, OnAddToCartListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_user, parent, false);
        return new ProductViewUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewUserHolder holder, int position) {
        Product product = products.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("Price: " + product.getPrice());
        holder.buttonAddToCart.setOnClickListener(v -> listener.onAddToCart(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewUserHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice;
        Button buttonAddToCart;

        ProductViewUserHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewProductName);
            textViewPrice = itemView.findViewById(R.id.textViewProductPrice);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }
    }

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }
}

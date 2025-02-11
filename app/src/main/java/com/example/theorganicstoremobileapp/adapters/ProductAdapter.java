package com.example.theorganicstoremobileapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.AddProductActivity;
import com.example.theorganicstoremobileapp.models.Product;
import com.example.theorganicstoremobileapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText("Name: " + product.getName() + " " + product.getLastName());
        holder.productPrice.setText(
                "Date of registration: " + product.getDateOfRegistration() +
                "\nPrice: " + product.getPrice() +
                "\nCategory: " + product.getCategory() +
                "\nSubcategory: " + product.getSubcategory()
        );
        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddProductActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("productName", product.getName());
            intent.putExtra("productPrice", product.getPrice());
            intent.putExtra("productCategory", product.getCategory());
            intent.putExtra("productSubcategory", product.getSubcategory());
            intent.putExtra("productLastName", product.getLastName());
            intent.putExtra("productDate", product.getDateOfRegistration());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnDeleteProduct.setOnClickListener(v -> {
            // Delete product from the database
            FirebaseFirestore.getInstance().collection("products")
                    .document(product.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the product from the list
                        productList.remove(product);
                        Toast.makeText(holder.itemView.getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        Button btnEditProduct;
        Button btnDeleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            btnEditProduct = itemView.findViewById(R.id.edit_product);
            btnDeleteProduct = itemView.findViewById(R.id.delete_product);
        }
    }

    public  void filterList(List<Product> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }

}
